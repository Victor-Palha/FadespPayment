package com.victorpalha.fadesppayment.payments.controllers;

import com.victorpalha.fadesppayment.payments.controllers.errors.ErrorResponse;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({
            InvalidDocumentException.class,
            InvalidCardNumberException.class,
            CardNumberNotAllowedException.class,
            InvalidAmountException.class,
            PaymentStatusErrorException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException ex) {
        String code = switch (ex.getClass().getSimpleName()) {
            case "InvalidDocumentException" -> "INVALID_DOCUMENT";
            case "InvalidCardNumberException" -> "INVALID_CARD_NUMBER";
            case "CardNumberNotAllowedException" -> "CARD_NUMBER_NOT_ALLOWED";
            case "InvalidAmountException" -> "INVALID_AMOUNT";
            case "PaymentStatusErrorException" -> "INVALID_STATUS";
            default -> "BAD_REQUEST";
        };

        return buildErrorResponse(ex.getMessage(), code, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildErrorResponse(
                "Invalid parameter: " + ex.getName() + ". " + ex.getMessage(),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), "NOT_FOUND", HttpStatus.NOT_FOUND);
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
                new ErrorResponse("Validation Error", "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value(), fieldErrors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return buildErrorResponse("Invalid JSON", "MALFORMED_JSON", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildErrorResponse("Internal Server Error", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, String code, HttpStatus status) {
        return new ResponseEntity<>(
                new ErrorResponse(message, code, status.value()),
                status
        );
    }
}
