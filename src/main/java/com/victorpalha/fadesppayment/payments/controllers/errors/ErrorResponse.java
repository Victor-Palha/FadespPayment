package com.victorpalha.fadesppayment.payments.controllers.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String code;
    private int status;
    private Map<String, String> fieldErrors;

    public ErrorResponse(String message, String code, int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
