package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class InvalidCardNumberException extends RuntimeException {
    public InvalidCardNumberException(String cardNumber) {
        super("Invalid card number: " + cardNumber + ". Failed Luhn check.");
    }
}
