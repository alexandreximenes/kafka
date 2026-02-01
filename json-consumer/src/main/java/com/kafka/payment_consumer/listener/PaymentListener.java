package com.kafka.payment_consumer.listener;

import com.kafka.payment_consumer.model.PaymentEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

@Component
@Slf4j
public class PaymentListener {

    @SneakyThrows
    @KafkaListener(topics = "payment-topic", groupId = "payment-group", containerFactory = "jsonSingleConsumerContainerFactory")
    public void recebePagamento1(@Payload PaymentEvent paymentEvent){
        sleep(1000);
        log.info("Pagamento recebido 1: {}", paymentEvent);

        sleep(2000);
        log.info("Processando pagamento 1: {}", paymentEvent.getId());
    }

    @SneakyThrows
    @KafkaListener(topics = "payment-topic", groupId = "payment-group", containerFactory = "jsonSingleConsumerContainerFactory")
    public void recebePagamento2(@Payload PaymentEvent paymentEvent){
        sleep(1000);
        log.info("Pagamento recebido 2: {}", paymentEvent);

        sleep(2000);
        log.info("Processando pagamento 2: {}", paymentEvent.getId());
    }

    @SneakyThrows
    @KafkaListener(topics = "payment-topic", groupId = "email-group", containerFactory = "jsonSingleConsumerContainerFactory")
    public void enviaEmail(@Payload PaymentEvent paymentEvent){
        sleep(4000);
        log.info("Pagamento Email: {}", paymentEvent.getId());

        sleep(5000);
        log.info("Enviando email: {}", paymentEvent.getId());
    }

    @SneakyThrows
    @KafkaListener(topics = "payment-topic", groupId = "relatorio-group", containerFactory = "jsonSingleConsumerContainerFactory")
    public void gerandoRelatorio(@Payload PaymentEvent paymentEvent){
        sleep(7000);
        log.info("Pagamento Relatorio: {}", paymentEvent.getId());

        sleep(8000);
        log.info("Gerando relatorio do valor: {}", paymentEvent.getAmount());
    }

    @SneakyThrows
    @KafkaListener(topics = "payment-topic", groupId = "logs-group", containerFactory = "jsonBatchConsumerContainerFactory")
    public void logs(@Payload PaymentEvent paymentEvent){
        sleep(10000);
        log.info("Payment log: {}", paymentEvent);
    }
}
