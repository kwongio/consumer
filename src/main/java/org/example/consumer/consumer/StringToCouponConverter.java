package org.example.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToCouponConverter {
    private final ObjectMapper objectMapper;

    public CouponAssignRequest convert(String coupon) {
        try {
            return objectMapper.readValue(coupon, CouponAssignRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
