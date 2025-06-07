package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class InvalidCardNumberException extends RuntimeException {
    public InvalidCardNumberException() {
        super("Invalid card number. Failed Luhn check.");
    }
}
