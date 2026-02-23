package com.learnavro.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learnavro.SchemaRegistryChoice;
import com.learnavro.domain.generated.*;
import io.apicurio.registry.serde.SerdeConfig;
import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import io.apicurio.registry.serde.strategy.TopicIdStrategy;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class Producer {

    private static final String TOPIC = "coffee.order";
    private static final Logger log = LoggerFactory.getLogger(Producer.class);
    public static final String ACKS_CONFIG_ALL = "all";
    static ObjectMapper mapper = new ObjectMapper();
    static KafkaProducer<String, String> producerJson = null;
    static KafkaProducer<OrderId, CoffeeOrder> producerAvro = null;

    public static void main(String[] args) throws ExecutionException, InterruptedException, JsonProcessingException {
        Properties properties = new Properties();
        setStandardProperties(properties);

        if(SchemaRegistryChoice.SCHEMA_REGISTRY_OFF){
            executeSchemaRegistryOff(properties);
        } else {
            executeSchemaRegistryOn(properties);
        }
    }

    private static Properties setStandardProperties(Properties properties) {
        properties.putIfAbsent(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG_ALL);
        // Número de tentativas em caso de falha transiente (rede, eleição de líder)
        properties.putIfAbsent(ProducerConfig.RETRIES_CONFIG, 10);
        // Garante que a ordem das mensagens seja mantida mesmo com retries (essencial para Java 21+)
        properties.putIfAbsent(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        // Agrupa mensagens em lotes de 64KB antes de enviar (melhora muito o desempenho)
        properties.putIfAbsent(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(64 * 1024));
        // Espera até 1000ms para encher o lote antes de disparar o envio
        properties.putIfAbsent(ProducerConfig.LINGER_MS_CONFIG, "1000");
        // Compactação: Reduz drasticamente o uso de rede e disco
        properties.putIfAbsent(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4"); //snappy
        // Tempo máximo que o .send().get() vai esperar antes de estourar erro
        properties.putIfAbsent(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "120000");
        // Tempo máximo para tentar conectar ao broker
        properties.putIfAbsent(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, "1000");

        return properties;
    }

    private static void executeSchemaRegistryOn(Properties properties) throws InterruptedException, ExecutionException {
        if(SchemaRegistryChoice.SCHEMA_REGISTRY_CONFLUENT){
            // Schema Registry Confluent
            log.info("SCHEMA REGISTRY ::: CONFLUENT ::: INICIADO");
            properties.putIfAbsent(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
            properties.putIfAbsent(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
            properties.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer .class.getName());
            properties.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            properties.putIfAbsent(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class.getName());
            properties.putIfAbsent("specific.avro.reader", "true");
        } else {
            log.info("SCHEMA REGISTRY ::: APICURIO ::: INICIADO");
            //Schema Registry Apicurio
            properties.putIfAbsent(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            properties.putIfAbsent(SerdeConfig.REGISTRY_URL, "http://localhost:8081/apis/registry/v3");
            properties.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class.getName());
            properties.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class.getName());
            properties.put(SerdeConfig.ARTIFACT_RESOLVER_STRATEGY, TopicIdStrategy.class.getName());
            properties.putIfAbsent(SerdeConfig.AUTO_REGISTER_ARTIFACT, true);
            properties.putIfAbsent("apicurio.registry.artifact.type", "AVRO");
            properties.putIfAbsent("auto.register.schemas", true);
            properties.putIfAbsent("specific.avro.reader", true);
        }

        producerAvro = new KafkaProducer<>(properties);
        var count = 0;
        while (true){
            Thread.sleep(Duration.ofSeconds(2));
            CoffeeOrder coffeeOrder = builderCoffeeOrder(count);

            ProducerRecord<OrderId, CoffeeOrder> producerRecord = new ProducerRecord<>(TOPIC, coffeeOrder.getOrderId(), coffeeOrder);
            producerRecord.headers().add(new RecordHeader("correlation-id", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
            producerRecord.headers().add(new RecordHeader("event-type", "CoffeeOrder".getBytes(StandardCharsets.UTF_8)));
            RecordMetadata metadata = producerAvro.send(producerRecord).get();
            log.info("offset: {}, partition: {}, topic: {}", metadata.offset(), metadata.partition(), metadata.topic());
            count++;
        }
    }

    private static void executeSchemaRegistryOff(Properties properties) throws InterruptedException, JsonProcessingException, ExecutionException {
        log.info("SCHEMA REGISTRY ::: DESABILITADO :::");
        properties.putIfAbsent(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SchemaRegistryChoice.SCHEMA_REGISTRY_CONFLUENT ? "localhost:29092" : "localhost:9092");
        properties.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producerJson = new KafkaProducer<>(properties);
        var count = 0;
        while (true){
            Thread.sleep(Duration.ofSeconds(2));
            com.learnavro.domain.CoffeeOrder coffeeOrder = com.learnavro.domain.CoffeeOrder.builder(count);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String value = mapper.writeValueAsString(coffeeOrder);

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, coffeeOrder.getId(), value);
            producerRecord.headers().add(new RecordHeader("correlation-id", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
            producerRecord.headers().add(new RecordHeader("event-type", "CoffeeOrder".getBytes(StandardCharsets.UTF_8)));
            RecordMetadata metadata = producerJson.send(producerRecord).get();
            log.info("offset: {}, partition: {}, topic: {}", metadata.offset(), metadata.partition(), metadata.topic());
            count++;
        }
    }

    private static CoffeeOrder builderCoffeeOrder(int count) {
        Size[] values = Size.values();
        Size sortSize = values[ThreadLocalRandom.current().nextInt(values.length)];
        return CoffeeOrder.newBuilder()
                .setOrderedTime(Instant.now())
                .setOrderedDate(LocalDate.now())
                .setName("Alexandre " + count)
                .setId(UUID.randomUUID())
                .setMilk(true)
                .setNickName("xyymenes")
                .setOrderId(OrderId.newBuilder().setId( new Random().nextInt() * 1).build())
                .setStore(Store.newBuilder()
                        .setId(count * count)
                        .setAddress(Address.newBuilder()
                                .setAddressLine1("Rua " + new Random().nextInt())
                                .setCity("Cidade " + new Random().nextInt())
                                .setStateProvince("Estado " + new Random().nextInt())
                                .setCountry("Pais " + new Random().nextInt())
                                .setZip("Pais " + new Random().nextInt(100_000_000))
                                .build())
                        .build())
                .setOrderLineItems(List.of(OrderLineItem.newBuilder()
                        .setName("Cafe com Leite " + count)
                        .setCost(BigDecimal.valueOf(new Random().nextDouble() * 10).setScale(2, RoundingMode.HALF_UP))
                        .setQuantity(1)
                        .setSize(sortSize)
                        .build()))
                .build();
    }

}
