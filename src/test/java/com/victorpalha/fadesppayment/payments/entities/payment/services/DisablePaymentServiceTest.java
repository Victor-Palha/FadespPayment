package com.victorpalha.fadesppayment.payments.entities.payment.services;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.PaymentNotFoundException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.PaymentStatusErrorException;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import com.victorpalha.fadesppayment.payments.services.DisablePaymentService;
import com.victorpalha.fadesppayment.payments.services.UpdatePaymentStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DisablePaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private DisablePaymentService disablePaymentService;

    private PaymentModel mockPayment;

    @BeforeEach
    void setup() {
        mockPayment = new PaymentModel();

        mockPayment.setId(UUID.randomUUID());
        mockPayment.setDebitCode(123L);
        mockPayment.setDocumentId("12345678901");
        mockPayment.setAmount(new BigDecimal("100.00"));
        mockPayment.setPaymentStatus(PaymentStatusType.PENDENTE);
        mockPayment.setActive(true);
    }

    @Test
    void shouldUpdateActiveSuccessfully() {
        when(paymentRepository.findById(mockPayment.getId()))
                .thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(mockPayment))
                .thenReturn(mockPayment);

        PaymentModel result = disablePaymentService.execute(mockPayment.getId());

        assertFalse(result.isActive());
    }

    @Test
    void shouldNotUpdateActiveSuccessfully() {
        mockPayment.setPaymentStatus(PaymentStatusType.PROCESSADO_SUCESSO);
        when(paymentRepository.findById(mockPayment.getId()))
                .thenReturn(Optional.of(mockPayment));

        assertThrows(PaymentStatusErrorException.class, () ->
                disablePaymentService.execute(mockPayment.getId()));
    }
}
