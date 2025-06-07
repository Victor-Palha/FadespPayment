package com.victorpalha.fadesppayment.payments.entities.payment;

import com.victorpalha.fadesppayment.payments.entities.payment.enums.DocumentType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.CardNumberNotAllowedException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidCardNumberException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidDocumentException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentModelTest {
    @Test
    void shouldSetDocumentTypeToCPF_WhenDocumentIdIs11Digits() {
        PaymentModel payment = new PaymentModel(
                1L, "12345678901", PaymentType.PIX, new BigDecimal("100.00"), null
        );

        assertEquals(DocumentType.CPF, payment.getDocumentType());
    }

    @Test
    void shouldSetDocumentTypeToCNPJ_WhenDocumentIdIs14Digits() {
        PaymentModel payment = new PaymentModel(
                1L, "12345678000199", PaymentType.PIX, new BigDecimal("200.00"), null
        );

        assertEquals(DocumentType.CNPJ, payment.getDocumentType());
    }

    @Test
    void shouldThrowException_WhenDocumentIdHasInvalidLength() {
        assertThrows(InvalidDocumentException.class, () -> {
            new PaymentModel(1L, "123", PaymentType.PIX, new BigDecimal("150.00"), null);
        });
    }

    @Test
    void shouldAcceptValidCardNumber_WhenPaymentTypeIsCreditCard() {
        PaymentModel payment = new PaymentModel(
                1L, "12345678901", PaymentType.CARTAO_CREDITO, new BigDecimal("300.00"), "4111111111111111"
        );

        assertEquals("4111111111111111", payment.getCreditCardNumber());
    }

    @Test
    void shouldThrowException_WhenCardNumberIsInvalid() {
        assertThrows(InvalidCardNumberException.class, () -> {
            new PaymentModel(
                    1L, "12345678901", PaymentType.CARTAO_CREDITO, new BigDecimal("300.00"), "1234567890123456"
            );
        });
    }

    @Test
    void shouldThrowException_WhenCardNumberIsUsedWithPix() {
        assertThrows(CardNumberNotAllowedException.class, () -> {
            new PaymentModel(
                    1L, "12345678901", PaymentType.PIX, new BigDecimal("300.00"), "4111111111111111"
            );
        });
    }
}
