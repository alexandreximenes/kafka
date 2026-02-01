package com.kafka.payment_consumer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PaymentEvent implements Serializable {

    private Long id;
    private Long idUser;
    private Long idProdutct;
    private BigDecimal amount;
    private String cardNumber;
}
