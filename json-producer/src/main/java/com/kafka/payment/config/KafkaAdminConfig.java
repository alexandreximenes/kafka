package com.kafka.payment.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile({"local", "default"})
public class KafkaAdminConfig {

    private final KafkaProperties properties;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        log.info("KAFKA ADMIN CONFIG ::: bootstrap servers: {}", properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics topics(){
        NewTopic newTopic = TopicBuilder.name("payment-topic")
                .partitions(3)
                .replicas(1)
                .build();
        log.info("KAFKA CONFIG TOPIC ::: {}", newTopic);
        return new KafkaAdmin.NewTopics(newTopic);
    }
}
