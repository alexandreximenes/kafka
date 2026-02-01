package com.kafka.producer.services;

import com.kafka.producer.config.properties.KafkaProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProducerMessageService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProperty property;

    public void sendMessage(String message) {
        kafkaTemplate.send(property.topic().name(), message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        var recordMeta = result.getRecordMetadata();
                        log.info("Mensagem enviada com sucesso: {}", message);
                        log.info("topic: {}, partition: {}, offset: {}",
                                recordMeta.topic(),
                                recordMeta.partition(),
                                recordMeta.offset());
                    } else {
                        log.error("Falha ao entregar mensagem", ex);
                    }
                });
    }
}
