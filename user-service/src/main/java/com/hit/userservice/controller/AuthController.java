package com.hit.userservice.controller;

import com.hit.userservice.base.RestApiV1;
import com.hit.userservice.base.VsResponseUtil;
import com.hit.userservice.domain.dto.LoginRequestDto;
import com.hit.userservice.service.AuthService;
import com.hit.userservice.util.constant.ErrorMessage;
import com.hit.userservice.util.constant.UrlConstant;
import com.hit.userservice.domain.dto.CreateUserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Validated
@RestApiV1
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "API Login")
    @PostMapping(UrlConstant.Auth.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        return VsResponseUtil.ok(authService.login(request));
    }

    @Operation(summary = "API Logout")
    @PostMapping(UrlConstant.Auth.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return VsResponseUtil.ok(authService.logout(request));
    }

    @Operation(summary = "(1) API SignUp and send mail token")
    @PostMapping(UrlConstant.Auth.SIGNUP)
    public ResponseEntity<?> signUp(@Valid @RequestBody CreateUserRequestDto userCreateDTO) {
        return VsResponseUtil.ok(authService.signUp(userCreateDTO));
    }

    @Operation(summary = "(2) API verify signup")
    @PostMapping(UrlConstant.Auth.VERIFY_SIGNUP)
    public ResponseEntity<?> verifySignUp(@RequestParam("email") String email, @RequestParam("token") String token) {
        return VsResponseUtil.ok(authService.verifySignUp(email, token));
    }

    @Operation(summary = "(1) API forgot password")
    @PostMapping(UrlConstant.Auth.FORGOT_PASS)
    public ResponseEntity<?> forgotPassword(@Email(message = ErrorMessage.INVALID_FORMAT_EMAIL) @RequestParam("email") String email) {
        return VsResponseUtil.ok(authService.forgotPassword(email));
    }

    @Operation(summary = "(2) API verify forgot password")
    @PostMapping(UrlConstant.Auth.VERIFY_FORGOT_PASS)
    public ResponseEntity<?> verifyForgotPassword(
            @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL) @RequestParam("email") String email,
            @RequestParam(name = "token") String token,
            @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$", message = ErrorMessage.INVALID_FORMAT_PASSWORD)
            @RequestParam(name = "password") String newPassword) {
        return VsResponseUtil.ok(authService.verifyForgotPassword(email, token, newPassword));
    }
}
