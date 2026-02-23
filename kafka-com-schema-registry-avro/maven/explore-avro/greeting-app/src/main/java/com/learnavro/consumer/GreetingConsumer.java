package com.learnavro.consumer;

import com.learnavro.Greeting;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

public class GreetingConsumer {

    private static final String GREETING_TOPIC = "greeting";
    private static final Logger log = LoggerFactory.getLogger(GreetingConsumer.class);

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        // Configuração dos Deserializers
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());

        properties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "greeting.consumer");

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, Greeting> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(GREETING_TOPIC));
        consumer.seekToBeginning(consumer.assignment());

        while(true){
            ConsumerRecords<String, Greeting> records = consumer.poll(Duration.ofMillis(1000));
            records.forEach(msg -> {
                if(Objects.nonNull(msg)){
                    try{
                       Greeting greeting = msg.value();
                       log.info("Topic: {}, Offset: {}, Greeting: {}",  msg.partition(), msg.offset(), greeting.toString());
                   }catch (Exception e){
                       log.error("Error {}", e.getMessage(), e);
                   }
                }
            });
            if (!records.isEmpty()) {
                consumer.commitSync();
            }
        }
    }

    //com a configuração VALUE_DESERIALIZER_CLASS_CONFIG e SPECIFIC_AVRO_READER_CONFIG a conversao de byes nao é ais necessaria
    private static Greeting decodeMessage(ConsumerRecord<String, byte[]> msg) throws IOException {
        return Greeting.fromByteBuffer(ByteBuffer.wrap(msg.value()));
    }
}
