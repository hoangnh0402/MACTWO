package com.hit.userservice.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    @NotBlank @Email(message = "Email không đúng định dạng")
    private String email;
    private String phoneNumber;
    @NotBlank @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
}
