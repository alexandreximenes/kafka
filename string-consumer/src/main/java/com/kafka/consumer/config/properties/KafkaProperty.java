package com.kafka.consumer.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperty(KafkaPropertiesTopic topic) { }