package com.hit.userservice.service.impl;

import com.hit.userservice.domain.dto.CreateUserRequestDto;
import org.springframework.security.core.GrantedAuthority;
import com.hit.userservice.domain.dto.JwtResponseDto;
import com.hit.userservice.domain.dto.LoginRequestDto;
import com.hit.userservice.domain.entity.AccountStatus;
import com.hit.userservice.domain.entity.ERole;
import com.hit.userservice.domain.entity.Role;
import com.hit.userservice.domain.entity.User;
import com.hit.userservice.event.UserRegisteredEvent;
import com.hit.userservice.exception.BadRequestException;
import com.hit.userservice.exception.ResourceNotFoundException;
import com.hit.userservice.payload.ApiResponse;
import com.hit.userservice.producer.UserEventProducer;
import com.hit.userservice.repository.RoleRepository;
import com.hit.userservice.repository.UserRepository;
import com.hit.userservice.security.JwtUtils;
import com.hit.userservice.security.UserDetailsImpl;
import com.hit.userservice.service.AuthService;
import com.hit.userservice.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final UserEventProducer userEventProducer;

    @Override
    public JwtResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Email hoặc mật khẩu không chính xác."));

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Tài khoản của bạn chưa được kích hoạt hoặc đã bị khóa.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponseDto(jwt, userDetails.getId(), userDetails.getEmail(), roles);
    }

    @Override
    public ApiResponse logout(HttpServletRequest request) {
        return new ApiResponse(true, "Đăng xuất thành công.");
    }

    @Override
    @Transactional
    public ApiResponse signUp(CreateUserRequestDto signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Lỗi: Email đã được sử dụng!");
        }
        User user = new User();
        user.setFullName(signUpRequest.getFullName());
        user.setEmail(signUpRequest.getEmail());
        user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        user.setAccountStatus(AccountStatus.PENDING_VERIFICATION);

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiryTime(LocalDateTime.now().plusHours(24));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy vai trò USER."));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), token);
        return new ApiResponse(true, "Đăng ký thành công. Vui lòng kiểm tra email để kích hoạt tài khoản.");
    }

    @Override
    @Transactional
    public ApiResponse verifySignUp(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email không hợp lệ."));

        if (user.getVerificationToken() == null || !user.getVerificationToken().equals(token)) {
            throw new BadRequestException("Token xác thực không hợp lệ.");
        }

        if (user.getVerificationTokenExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token xác thực đã hết hạn.");
        }

        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiryTime(null);
        User savedUser = userRepository.save(user);

        // Gửi sự kiện đến Kafka sau khi kích hoạt thành công
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();
        userEventProducer.sendUserRegisteredEvent(event);

        return new ApiResponse(true, "Tài khoản của bạn đã được kích hoạt thành công.");
    }

    @Override
    @Transactional
    public ApiResponse forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);
        return new ApiResponse(true, "Mã OTP đã được gửi đến email của bạn.");
    }

    @Override
    @Transactional
    public ApiResponse verifyForgotPassword(String email, String token, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (user.getOtp() == null || !user.getOtp().equals(token)) {
            throw new BadRequestException("Mã OTP không hợp lệ.");
        }

        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã OTP đã hết hạn.");
        }

        user.setPasswordHash(encoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);

        return new ApiResponse(true, "Mật khẩu đã được đặt lại thành công.");
    }
}
