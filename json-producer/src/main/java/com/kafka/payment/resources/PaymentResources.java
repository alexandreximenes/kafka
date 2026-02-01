package com.kafka.payment.resources;

import com.kafka.payment.model.PaymentEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PaymentResources {

    @PostMapping
    ResponseEntity<PaymentEvent> payment(@RequestBody PaymentEvent paymentEvent);
}
