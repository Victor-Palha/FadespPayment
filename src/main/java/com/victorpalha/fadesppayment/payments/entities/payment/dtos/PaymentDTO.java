package com.victorpalha.fadesppayment.payments.entities.payment.dtos;

import com.victorpalha.fadesppayment.payments.entities.payment.enums.DocumentType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Debit Code is required")
    private Long debitCode;

    @Size(min = 14, max = 16, message = "Invalid card number. Failed Luhn check.")
    private String creditCardNumber;

    @NotBlank(message = "Document Id is required")
    @Size(min = 11, message = "Invalid document ID.")
    private String documentId;

    @NotNull(message = "Payment Method is required")
    private PaymentType paymentMethod;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount should be greater than 0.")
    private BigDecimal amount;

    private PaymentStatusType paymentStatus;
    private DocumentType documentType;
    private boolean active = true;

    public void setAmount(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId.replaceAll("\\D", "");
    }
}
