package com.learnavro.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learnavro.SchemaRegistryChoice;
import com.learnavro.domain.generated.CoffeeOrder;
import com.learnavro.domain.generated.OrderId;
import io.apicurio.registry.serde.SerdeConfig;
import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import io.apicurio.registry.serde.strategy.TopicIdStrategy;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    private static final String TOPIC = "coffee.order";
    static KafkaConsumer<String, String> consumerJson = null;
    static KafkaConsumer<OrderId, CoffeeOrder> consumerAvro = null;
    static ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        setStandardProperties(properties);

        if (SchemaRegistryChoice.SCHEMA_REGISTRY_OFF) {
            executeSchemaRegistryOff(properties);
        } else {
            executeSchemaRegistryOn(properties);
        }
    }

    private static Properties setStandardProperties(Properties properties) {
        properties.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG, "coffee.consumer");
        properties.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    private static void executeSchemaRegistryOn(Properties properties) {
        if(SchemaRegistryChoice.SCHEMA_REGISTRY_CONFLUENT){
            // Schema Registry Confluent
            log.info("SCHEMA REGISTRY ::: CONFLUENT ::: INICIADO");
            properties.putIfAbsent(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
            properties.putIfAbsent(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
            properties.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
            properties.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
            properties.putIfAbsent(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        } else{
            //Schema Registry RedRat Apicurio
            log.info("SCHEMA REGISTRY ::: APICURIO ::: INICIADO");
            properties.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            properties.putIfAbsent(SerdeConfig.REGISTRY_URL, "http://localhost:8081/apis/registry/v3");
            properties.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, AvroKafkaDeserializer.class.getName());
            properties.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroKafkaDeserializer.class.getName());
            properties.putIfAbsent(SerdeConfig.AUTO_REGISTER_ARTIFACT, true);
            properties.put(SerdeConfig.ARTIFACT_RESOLVER_STRATEGY, TopicIdStrategy.class.getName());
            properties.putIfAbsent("apicurio.registry.artifact.type", "AVRO");
            properties.putIfAbsent("auto.register.schemas", true);
            properties.put("apicurio.registry.use-specific-avro-reader", true);
        }

        consumerAvro = new KafkaConsumer<>(properties);
        consumerAvro.subscribe(Collections.singleton(TOPIC));
        consumerAvro.seekToBeginning(consumerAvro.assignment());

        while(true){
            ConsumerRecords<OrderId, CoffeeOrder> records = consumerAvro.poll(Duration.ofMillis(1000));
            records.forEach(msg -> {
                if(Objects.nonNull(msg)){
                    try{
                        CoffeeOrder coffeeOrder = msg.value();
                        log.info("Topic: {}, Offset: {}, Coffee Order: {}",  msg.partition(), msg.offset(), coffeeOrder.toString());
                    }catch (Exception e){
                        log.error("Error {}", e.getMessage(), e);
                    }
                }
            });
            if (!records.isEmpty()) {
                consumerAvro.commitSync();
            }
        }
    }

    private static void executeSchemaRegistryOff(Properties properties) {
        log.info("SCHEMA REGISTRY ::: DESABILITADO :::");
        properties.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SchemaRegistryChoice.SCHEMA_REGISTRY_CONFLUENT ? "localhost:29092" : "localhost:9092");
        properties.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put("spring.json.trusted.packages", "*");

        consumerJson = new KafkaConsumer<>(properties);
        consumerJson.subscribe(Collections.singleton(TOPIC));
        consumerJson.seekToBeginning(consumerJson.assignment());

        while(true){
            ConsumerRecords<String, String> records = consumerJson.poll(Duration.ofMillis(1000));
            records.forEach(msg -> {
                if(Objects.nonNull(msg)){
                    try{
                        com.learnavro.domain.CoffeeOrder coffeeOrder = mapper.readValue(msg.value(),com.learnavro.domain.CoffeeOrder.class);
                        log.info("Topic: {}, Offset: {}, Coffee Order: {}",  msg.partition(), msg.offset(), coffeeOrder.toString());
                    }catch (Exception e){
                        log.error("Error {}", e.getMessage(), e);
                    }
                }
            });
            if (!records.isEmpty()) {
                consumerJson.commitSync();
            }
        }
    }
}
