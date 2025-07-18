package com.hit.userservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hit.userservice.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @purpose Lớp triển khai của UserDetails, cung cấp thông tin cốt lõi của người dùng cho Spring Security.
 * Spring Security sử dụng thông tin từ lớp này để thực hiện xác thực và phân quyền.
 * @author Gemini
 * @date 2025-07-18
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer id;

    private final String email;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // Trong hệ thống này, chúng ta dùng email làm username
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Trả về true nếu tài khoản không bao giờ hết hạn
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Trả về true nếu tài khoản không bị khóa
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Trả về true nếu thông tin xác thực (mật khẩu) không bao giờ hết hạn
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Trả về true nếu tài khoản được kích hoạt
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
