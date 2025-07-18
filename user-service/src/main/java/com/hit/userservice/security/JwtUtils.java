package com.hit.userservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * @purpose Lớp tiện ích để xử lý các hoạt động liên quan đến JSON Web Tokens (JWT).
 * Bao gồm tạo token, xác thực token, và trích xuất thông tin từ token.
 * @author Gemini
 * @date 2025-07-18
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.time_expiration}")
    private long jwtExpirationInMinutes;

    /**
     * @purpose Tạo ra một chuỗi JWT từ thông tin xác thực của người dùng.
     * @author Gemini
     * @param authentication - Đối tượng Authentication chứa thông tin người dùng đã đăng nhập thành công.
     * @return String - Chuỗi JWT đã được ký.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        long expirationTimeInMillis = jwtExpirationInMinutes * 60 * 1000;

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Đặt email của người dùng làm subject
                .setIssuedAt(new Date()) // Thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis)) // Thời gian hết hạn
                .signWith(key(), SignatureAlgorithm.HS256) // Ký token với thuật toán HS256
                .compact();
    }

    /**
     * @purpose Tạo khóa ký (signing key) từ chuỗi secret đã được mã hóa Base64.
     * @author Gemini
     * @return Key - Đối tượng Key dùng cho việc ký và xác thực.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * @purpose Trích xuất email (username) từ một chuỗi JWT.
     * @author Gemini
     * @param token - Chuỗi JWT cần giải mã.
     * @return String - Email của người dùng.
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * @purpose Kiểm tra tính hợp lệ của một chuỗi JWT.
     * @author Gemini
     * @param authToken - Chuỗi JWT cần kiểm tra.
     * @return boolean - Trả về true nếu token hợp lệ, ngược lại trả về false.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Chữ ký JWT không hợp lệ: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT token không hợp lệ: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token đã hết hạn: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token không được hỗ trợ: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Chuỗi JWT claims trống: {}", e.getMessage());
        }

        return false;
    }
}
