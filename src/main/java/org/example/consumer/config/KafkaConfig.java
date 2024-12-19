package org.example.consumer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConfig {

    public static final int DEFAULT_BACKOFF_MULTI = 2;
    public static final int DEFAULT_BACKOFF_INITIAL_INTERVAL = 1000;
    public static final int DEFAULT_BACKOFF_MAX_ATTEMPTS = 5;
    public static final String DEFAULT_AUTO_OFFSET_RESET_CONFIG = "earliest";
    public static final int DEFAULT_MAX_POLL_RECORDS_CONFIG = 500;
    public static final String DEFAULT_ACKS_CONFIG = "all";
    public static final String DEFAULT_KAFKA_BROKER_SERVER = "localhost:9092,localhost:9093,localhost:9094";
    public static final String DEFAULT_CONSUMER_GROUP = "coupon_system";

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT_KAFKA_BROKER_SERVER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, DEFAULT_CONSUMER_GROUP);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, DEFAULT_AUTO_OFFSET_RESET_CONFIG);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, DEFAULT_MAX_POLL_RECORDS_CONFIG);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public DefaultKafkaProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, DEFAULT_KAFKA_BROKER_SERVER);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.ACKS_CONFIG, DEFAULT_ACKS_CONFIG);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setBatchListener(true);

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate()), exponentialBackOff());
        defaultErrorHandler.addNotRetryableExceptions(JsonProcessingException.class);
        factory.setCommonErrorHandler(defaultErrorHandler);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate()), exponentialBackOff());
        defaultErrorHandler.addNotRetryableExceptions(JsonProcessingException.class);
        factory.setCommonErrorHandler(defaultErrorHandler);

        return factory;
    }

    private BackOff exponentialBackOff() {
        ExponentialBackOff backOff = new ExponentialBackOff(DEFAULT_BACKOFF_INITIAL_INTERVAL, DEFAULT_BACKOFF_MULTI);
        backOff.setMaxAttempts(DEFAULT_BACKOFF_MAX_ATTEMPTS);
        return backOff;
    }
}
