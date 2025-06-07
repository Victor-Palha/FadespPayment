package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
      super("Amount should be greater than 0.");
    }
}
