package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class PaymentStatusErrorException extends RuntimeException {
    public PaymentStatusErrorException(String message) {
        super(message);
    }
}
