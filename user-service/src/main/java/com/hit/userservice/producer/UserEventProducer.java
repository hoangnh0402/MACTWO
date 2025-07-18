package com.hit.userservice.producer;

import com.hit.userservice.config.KafkaTopicConfig;
import com.hit.userservice.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            log.info("Đang gửi sự kiện đăng ký người dùng: {}", event);
            kafkaTemplate.send(KafkaTopicConfig.USER_REGISTERED_TOPIC, event);
            log.info("Gửi sự kiện thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện đăng ký người dùng: ", e);
        }
    }
}
