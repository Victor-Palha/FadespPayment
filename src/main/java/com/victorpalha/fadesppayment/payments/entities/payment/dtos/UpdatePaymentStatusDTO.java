package com.victorpalha.fadesppayment.payments.entities.payment.dtos;

import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import jakarta.validation.constraints.NotNull;

public class UpdatePaymentStatusDTO {
    @NotNull
    private PaymentStatusType status;

    public PaymentStatusType getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusType status) {
        this.status = status;
    }
}
