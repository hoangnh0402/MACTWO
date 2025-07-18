package com.hit.userservice.service;

import com.hit.userservice.domain.dto.CreateUserRequestDto;
import com.hit.userservice.domain.dto.JwtResponseDto;
import com.hit.userservice.domain.dto.LoginRequestDto;
import com.hit.userservice.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    JwtResponseDto login(LoginRequestDto loginRequest);

    ApiResponse logout(HttpServletRequest request);

    ApiResponse signUp(CreateUserRequestDto signUpRequest);

    ApiResponse verifySignUp(String email, String token);

    ApiResponse forgotPassword(String email);

    ApiResponse verifyForgotPassword(String email, String token, String newPassword);

}
