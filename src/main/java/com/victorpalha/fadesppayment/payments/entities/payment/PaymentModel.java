package com.victorpalha.fadesppayment.payments.entities.payment;

import com.victorpalha.fadesppayment.payments.entities.payment.enums.DocumentType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.CardNumberNotAllowedException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidAmountException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidCardNumberException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidDocumentException;
import com.victorpalha.fadesppayment.payments.entities.payment.helpers.LuhnValidation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "tb_payments")
@Entity
@NoArgsConstructor
@Data
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Long debitCode;
    @Length(min = 14, max = 16)
    private String creditCardNumber;
    @Length(min = 11, max = 14)
    private String documentId;
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus = PaymentStatusType.PENDENTE;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;
    private boolean active = true;

    public PaymentModel(Long debitCode, String documentId, PaymentType paymentMethod, BigDecimal amount, String creditCardNumber) {
        this.debitCode = debitCode;
        this.setDocumentId(documentId);
        this.paymentMethod = paymentMethod;
        this.setCreditCardNumber(creditCardNumber);
        this.amount = amount;
        this.paymentStatus = PaymentStatusType.PENDENTE;
        this.active = true;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;

        if (documentId == null) return;

        switch (documentId.length()) {
            case 11 -> this.documentType = DocumentType.CPF;
            case 14 -> this.documentType = DocumentType.CNPJ;
            default -> throw new InvalidDocumentException();
        }
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;

        boolean isCardPayment = this.paymentMethod == PaymentType.CARTAO_CREDITO || this.paymentMethod == PaymentType.CARTAO_DEBITO;

        if (!isCardPayment && creditCardNumber != null) {
            throw new CardNumberNotAllowedException();
        }

        if (isCardPayment) {
            if (creditCardNumber == null || !LuhnValidation.execute(creditCardNumber)) {
                throw new InvalidCardNumberException();
            }
        }
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidAmountException();
        }
        this.amount = amount;
    }
}
