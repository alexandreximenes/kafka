package com.kafka.consumer.config.properties;

public record KafkaPropertiesTopic(
        String name,
        int partitions,
        short replicas
) {}
