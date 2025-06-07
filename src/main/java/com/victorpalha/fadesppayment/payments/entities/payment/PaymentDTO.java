package com.victorpalha.fadesppayment.payments.entities.payment;

import com.victorpalha.fadesppayment.payments.entities.payment.enums.DocumentType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private UUID id;
    private Long debitCode;
    private String creditCardNumber;
    private String documentId;
    private PaymentType paymentMethod;
    private BigDecimal amount;
    private PaymentStatusType paymentStatus;
    private DocumentType documentType;
    private boolean active;
}
