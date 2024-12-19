package org.example.consumer.consumer;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponAssignRequest {
    private Long couponId;
    private UUID uuid;
}
