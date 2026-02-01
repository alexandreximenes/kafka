package com.kafka.producer.config;

import com.kafka.producer.config.properties.KafkaProperty;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@RequiredArgsConstructor
@Configuration
public class KafkaAdminConfig {

    private final KafkaProperty property;
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics topics(){
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(property.topic().name())
                        .partitions(property.topic().partitions())
                        .replicas(property.topic().replicas())
                        .build()
        );
    }
}
