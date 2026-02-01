package com.kafka.payment.resources.impl;

import com.kafka.payment.model.PaymentEvent;
import com.kafka.payment.resources.PaymentResources;
import com.kafka.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentResouresImpl implements PaymentResources {

    private final PaymentService paymentService;
    @Override
    public ResponseEntity<PaymentEvent> payment(@RequestBody PaymentEvent paymentEvent) {
        paymentService.sendPaymentMessage(paymentEvent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
