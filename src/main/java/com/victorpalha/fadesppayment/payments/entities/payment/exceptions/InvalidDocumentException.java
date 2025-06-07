package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class InvalidDocumentException extends RuntimeException{
    public InvalidDocumentException(String documentId) {
        super("Invalid document ID: " + documentId + ". Must be 11 (CPF) or 14 (CNPJ) digits.");
    }
}
