package com.hit.userservice.service.impl;

import com.hit.userservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Yêu cầu đặt lại mật khẩu MacOne");
            message.setText("Mã OTP của bạn là: " + otp + ". Mã này sẽ hết hạn sau 5 phút.");
            mailSender.send(message);
            log.info("Đã gửi email OTP đến {}", to);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email OTP đến {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendVerificationEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Xác thực tài khoản MacOne");
            message.setText("Chào mừng bạn đến với MacOne! Vui lòng sử dụng token sau để kích hoạt tài khoản của bạn: " + token);
            mailSender.send(message);
            log.info("Đã gửi email xác thực đến {}", to);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email xác thực đến {}: {}", to, e.getMessage());
        }
    }
}
