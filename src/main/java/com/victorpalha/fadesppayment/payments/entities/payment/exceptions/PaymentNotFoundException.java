package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
      super("Payment Not Found");
    }
}
