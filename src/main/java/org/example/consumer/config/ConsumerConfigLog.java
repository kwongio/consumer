package org.example.consumer.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsumerConfigLog {

    private final ConsumerFactory<?, ?> consumerFactory;

    @PostConstruct
    public void printConsumerConfig() {
        log.info("Consumer config: {}", consumerFactory.getConfigurationProperties());
    }
}
