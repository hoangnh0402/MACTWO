package com.hit.userservice.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
    void sendVerificationEmail(String to, String token);
}
