package com.kafka.payment_consumer.config;

import com.kafka.payment_consumer.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JsonConsumerConfig {

    private final KafkaProperties properties;

    @Bean
    public ConsumerFactory<String, Object> jsonConsumerFactory(){
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        // 🔑 ErrorHandling
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // evita erro com headers ausentes
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentEvent.class);
        configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configs.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> jsonSingleConsumerContainerFactory(ConsumerFactory<String, Object> jsonConsumerFactory){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(jsonConsumerFactory);
//        factory.setRecordInterceptor(interceptorMessage());
        return factory;
    }

    private RecordInterceptor<String, Object> interceptorMessage() {
        return (record, consumer) -> {
            log.info("---------------- Interceptor Message ----------------");
            log.info("Topic ::: {}, OffSet ::: {}, Partition ::: {}", record.topic(), record.offset(), record.partition());
            log.info("serializedKeySize ::: {}, serializedValueSize ::: {}", record.serializedKeySize(), record.serializedValueSize());
            log.info("Headers ::: {}", record.headers());
//            consumer.commitAsync();
//            log.info(consumer.metrics().toString());
            return record;
        };
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> jsonBatchConsumerContainerFactory(ConsumerFactory<String, Object> jsonConsumerFactory){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(jsonConsumerFactory);
        factory.setBatchMessageConverter(new BatchMessagingMessageConverter());
        return factory;
    }
}
