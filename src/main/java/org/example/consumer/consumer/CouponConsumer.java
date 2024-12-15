package org.example.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponConsumer {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "coupon-assign", groupId = "coupon-assign-group")
    public void listenAssignCouponRequest(String message) {
        try {
            CouponAssignRequest couponAssignRequest = objectMapper.readValue(message, CouponAssignRequest.class);
            log.info("Received assign coupon request: {}", couponAssignRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
