package com.kafka.payment.services;


import com.kafka.payment.model.PaymentEvent;

public interface PaymentService {

    void sendPaymentMessage(PaymentEvent paymentEvent);
}
