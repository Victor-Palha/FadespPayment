package com.victorpalha.fadesppayment.payments.entities.payment.services;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import com.victorpalha.fadesppayment.payments.services.FetchPaymentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FetchPaymentsServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private FetchPaymentsService fetchPaymentsService;

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
    void shouldReturnAllPaymentsWhenNoFilterProvided() {
        when(paymentRepository.findByFilters(null, null, null)).thenReturn(List.of(mockPayment, mockPayment));

        var result = fetchPaymentsService.execute(null, null, null);

        assertEquals(2, result.size());
        verify(paymentRepository).findByFilters(null, null, null);
    }

    @Test
    void shouldFilterByDebitCode() {
        Long debitCode = 123L;
        when(paymentRepository.findByFilters(debitCode, null, null)).thenReturn(List.of(mockPayment));

        var result = fetchPaymentsService.execute(debitCode, null, null);

        assertEquals(1, result.size());
        assertEquals(debitCode, result.get(0).getDebitCode());
    }

    @Test
    void shouldFilterByDocumentId() {
        String documentId = "12345678901";
        when(paymentRepository.findByFilters(null, documentId, null)).thenReturn(List.of(mockPayment));

        var result = fetchPaymentsService.execute(null, documentId, null);

        assertEquals(1, result.size());
        assertEquals(documentId, result.get(0).getDocumentId());
    }

    @Test
    void shouldFilterByPaymentStatus() {
        PaymentStatusType status = PaymentStatusType.PROCESSADO_SUCESSO;
        mockPayment.setPaymentStatus(status);
        when(paymentRepository.findByFilters(null, null, status)).thenReturn(List.of(mockPayment));

        var result = fetchPaymentsService.execute(null, null, status);

        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getPaymentStatus());
    }
}
