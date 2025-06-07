package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class InvalidDocumentException extends RuntimeException{
    public InvalidDocumentException() {
        super("Invalid document ID. Must be 11 (CPF) or 14 (CNPJ) digits.");
    }
}
