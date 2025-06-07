package com.victorpalha.fadesppayment.payments.entities.payment;

import com.victorpalha.fadesppayment.payments.entities.payment.enums.DocumentType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.CardNumberNotAllowedException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidCardNumberException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidDocumentException;
import com.victorpalha.fadesppayment.payments.entities.payment.helpers.LuhnValidation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private UUID id;
    @Getter
    private Long debitCode;
    @Length(min = 14, max = 16)
    @Getter
    private String creditCardNumber;
    @Length(min = 11, max = 14)
    @Getter
    private String documentId;
    @Enumerated(EnumType.STRING)
    @Getter
    private DocumentType documentType;
    @Enumerated(EnumType.STRING)
    @Getter
    private PaymentStatusType paymentStatus = PaymentStatusType.PENDENTE;
    @Enumerated(EnumType.STRING)
    @Getter
    private PaymentType paymentMethod;
    @Column(precision = 12, scale = 2)
    @Getter
    private BigDecimal amount;
    @Getter
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

        if (documentId != null) {
            if (documentId.length() == 11) {
                this.documentType = DocumentType.CPF;
            } else if (documentId.length() == 14) {
                this.documentType = DocumentType.CNPJ;
            } else {
                throw new InvalidDocumentException(documentId);
            }
        }
    }

    public void setCreditCardNumber(String creditCardNumber) {
        if (creditCardNumber != null) {
            if (this.paymentMethod == PaymentType.CARTAO_CREDITO || this.paymentMethod == PaymentType.CARTAO_DEBITO) {
                if (!LuhnValidation.execute(creditCardNumber)) {
                    throw new InvalidCardNumberException(creditCardNumber);
                }
                this.creditCardNumber = creditCardNumber;
            } else {
                throw new CardNumberNotAllowedException();
            }
        }
    }
}
