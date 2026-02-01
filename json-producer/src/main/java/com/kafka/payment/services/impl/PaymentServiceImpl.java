package com.kafka.payment.services.impl;

import com.kafka.payment.model.PaymentEvent;
import com.kafka.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    @SneakyThrows
    @Override
    public void sendPaymentMessage(PaymentEvent paymentEvent) {
        log.info("PAYMENT_SERVICE_IMPL ::: pagamento recebido: {}", paymentEvent);
        Thread.sleep(2000);
        log.info("PAYMENT_SERVICE_IMPL ::: enviando pagamento: {}", paymentEvent);
        kafkaTemplate.send("payment-topic", paymentEvent)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        var recordMeta = result.getRecordMetadata();
                        log.info("PAYMENT_SERVICE_IMPL ::: pagamento enviado com sucesso: {}", paymentEvent);
                        log.info("PAYMENT_SERVICE_IMPL ::: topic: {}, partition: {}, offset: {}",
                                recordMeta.topic(),
                                recordMeta.partition(),
                                recordMeta.offset());
                    } else {
                        log.error("PAYMENT_SERVICE_IMPL ::: Falha ao entregar pagamento: {}", paymentEvent, ex);
                    }
                });
    }
}
