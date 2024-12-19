package org.example.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.consumer.domain.CouponAssignLog;
import org.example.consumer.repository.CouponAssignLogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponConsumer {

    private final StringToCouponConverter stringToCouponConverter;
    private final CouponAssignLogRepository couponAssignRepository;

    @KafkaListener(topics = "coupon-assign", groupId = "coupon_system", containerFactory = "batchFactory")
    public void listenAssignCouponRequests(List<String> coupons) throws JsonProcessingException {
        List<CouponAssignLog> couponAssignLogs = coupons.stream()
                .map(stringToCouponConverter::convert)
                .map(request -> CouponAssignLog.builder()
                        .couponId(request.getCouponId())
                        .uuid(request.getUuid().toString())
                        .build())
                .toList();

        if (!couponAssignLogs.isEmpty()) {
            couponAssignRepository.saveAll(couponAssignLogs);
        }
    }


    @KafkaListener(topics = "coupon-assign-dlt", groupId = "coupon_system")
    public void couponDlt(String coupon) {
        CouponAssignRequest couponAssignRequest = stringToCouponConverter.convert(coupon);
        CouponAssignLog couponAssignLog = CouponAssignLog.builder()
                .couponId(couponAssignRequest.getCouponId())
                .uuid(couponAssignRequest.getUuid().toString())
                .build();
        couponAssignRepository.save(couponAssignLog);
    }
}