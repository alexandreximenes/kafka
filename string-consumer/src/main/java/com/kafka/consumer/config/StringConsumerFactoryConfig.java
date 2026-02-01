package com.kafka.consumer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StringConsumerFactoryConfig {

    private final KafkaProperties properties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory(){
        var configs = new HashMap<String, Object>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getConsumer().getKeyDeserializer());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getConsumer().getValueDeserializer());

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> containerFactory(ConsumerFactory<String, String> consumerFactory){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> validationMessageContainerFactory(ConsumerFactory<String, String> consumerFactory){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordInterceptor(interceptorMessage());
        return factory;
    }

    private RecordInterceptor<String, String> interceptorMessage() {
        return (record, consumer) -> {
            log.info("Topic ::: {}, OffSet ::: {}, Partition ::: {}", record.topic(), record.offset(), record.partition());
            log.info("serializedKeySize ::: {}, serializedValueSize ::: {}", record.serializedKeySize(), record.serializedValueSize());
            log.info("Headers ::: {}", record.headers());
//            if(record.value().contains("Teste")){
//                log.info("Intercepted message: {}", record.value());
//            }
            consumer.commitAsync();
//            log.info(consumer.metrics().toString());
            return record;
        };
    }
}
