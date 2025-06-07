package com.victorpalha.fadesppayment.payments.controllers;

import com.victorpalha.fadesppayment.payments.controllers.errors.ErrorResponse;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.CardNumberNotAllowedException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidAmountException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidCardNumberException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidDocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDocument(InvalidDocumentException ex){
        return buildErrorResponse(ex.getMessage(), "INVALID_DOCUMENT", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCardNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCard(InvalidCardNumberException ex) {
        return buildErrorResponse(ex.getMessage(), "INVALID_CARD_NUMBER", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CardNumberNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleCardNotAllowed(CardNumberNotAllowedException ex) {
        return buildErrorResponse(ex.getMessage(), "CARD_NUMBER_NOT_ALLOWED", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmount(InvalidAmountException ex) {
        return buildErrorResponse(ex.getMessage(), "INVALID_AMOUNT", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (msg1, msg2) -> msg1
                ));

        return new ResponseEntity<>(
                new ErrorResponse("Erro de validação", "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value(), fieldErrors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return buildErrorResponse("JSON malformado ou tipo de dado inválido", "MALFORMED_JSON", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildErrorResponse("Erro interno no servidor", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, String code, HttpStatus status) {
        return new ResponseEntity<>(
                new ErrorResponse(message, code, status.value()),
                status
        );
    }
}
