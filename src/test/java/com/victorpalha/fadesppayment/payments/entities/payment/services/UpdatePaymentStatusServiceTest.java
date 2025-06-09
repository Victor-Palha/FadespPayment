package com.victorpalha.fadesppayment.payments.entities.payment.services;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.PaymentNotFoundException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.PaymentStatusErrorException;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatePaymentStatusServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private UpdatePaymentStatusService updatePaymentStatusService;

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
    void shouldUpdateStatusSuccessfully() {
        mockPayment.setPaymentStatus(PaymentStatusType.PENDENTE);
        when(paymentRepository.findById(mockPayment.getId()))
                .thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(mockPayment))
                .thenReturn(mockPayment);

        PaymentModel result = updatePaymentStatusService.execute(
                mockPayment.getId(), PaymentStatusType.PROCESSADO_SUCESSO);

        assertEquals(PaymentStatusType.PROCESSADO_SUCESSO, result.getPaymentStatus());
        verify(paymentRepository).save(mockPayment);
    }

    @Test
    void shouldThrowWhenPaymentNotFound() {
        UUID randomPaymentId = UUID.randomUUID();
        when(paymentRepository.findById(randomPaymentId))
                .thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () ->
                updatePaymentStatusService.execute(randomPaymentId, PaymentStatusType.PENDENTE));
    }

    @Test
    void shouldThrowWhenStatusIsAlreadySuccess() {
        mockPayment.setPaymentStatus(PaymentStatusType.PROCESSADO_SUCESSO);
        when(paymentRepository.findById(mockPayment.getId()))
                .thenReturn(Optional.of(mockPayment));

        assertThrows(PaymentStatusErrorException.class, () ->
                updatePaymentStatusService.execute(mockPayment.getId(), PaymentStatusType.PENDENTE));
    }

    @Test
    void shouldThrowWhenStatusFailedAndTryingToChangeToNonPending() {
        mockPayment.setPaymentStatus(PaymentStatusType.PROCESSADO_FALHA);
        when(paymentRepository.findById(mockPayment.getId()))
                .thenReturn(Optional.of(mockPayment));

        assertThrows(PaymentStatusErrorException.class, () ->
                updatePaymentStatusService.execute(mockPayment.getId(), PaymentStatusType.PROCESSADO_SUCESSO));
    }

    @Test
    void shouldAllowChangeFromFailedToPending() {
        mockPayment.setPaymentStatus(PaymentStatusType.PROCESSADO_FALHA);
        when(paymentRepository.findById(mockPayment.getId()))
                .thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(mockPayment))
                .thenReturn(mockPayment);

        PaymentModel result = updatePaymentStatusService.execute(
                mockPayment.getId(), PaymentStatusType.PENDENTE);

        assertEquals(PaymentStatusType.PENDENTE, result.getPaymentStatus());
        verify(paymentRepository).save(mockPayment);
    }
}
