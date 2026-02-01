package com.kafka.producer.config.properties;

public record KafkaPropertiesTopic(
        String name,
        int partitions,
        short replicas
) {}
