package com.hit.userservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    private String phoneNumber;
}
