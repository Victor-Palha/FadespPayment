package com.victorpalha.fadesppayment.payments.entities.payment.services;
import com.victorpalha.fadesppayment.payments.entities.payment.dtos.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.dtos.PaymentMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.DocumentType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.CardNumberNotAllowedException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidAmountException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidCardNumberException;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.InvalidDocumentException;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import com.victorpalha.fadesppayment.payments.services.CreatePaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreatePaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CreatePaymentService createPaymentService;

    @Test
    void shouldCreatePaymentSuccessfully() {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("300.00");

        PaymentModel mappedModel = new PaymentMapper().map(dto);

        when(paymentRepository.save(mappedModel)).thenReturn(mappedModel);

        PaymentModel result = createPaymentService.execute(dto);
        assertNotNull(result);
        assertEquals(mappedModel.getId(), result.getId());
        assertEquals(mappedModel.getDebitCode(), result.getDebitCode());
        assertEquals(mappedModel.getCreditCardNumber(), result.getCreditCardNumber());
        assertEquals(DocumentType.CPF, mappedModel.getDocumentType());
        assertEquals(PaymentStatusType.PENDENTE, result.getPaymentStatus());
        assertEquals(mappedModel.getPaymentMethod(), result.getPaymentMethod());
        assertTrue(mappedModel.isActive());
    }

    @Test
    void shouldThrowExceptionForInvalidDocumentIdLength() {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("123");
        dto.setAmount("300.00");

        assertThrows(InvalidDocumentException.class, () -> {
            createPaymentService.execute(dto);
        });
    }

    @Test
    void shouldThrowExceptionForInvalidCreditCardNumber() {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("1111111111111111");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("300.00");

        assertThrows(InvalidCardNumberException.class, () -> {
            createPaymentService.execute(dto);
        });
    }

    @Test
    void shouldThrowExceptionWhenCardNumberProvidedForNonCardPayment() {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.BOLETO);
        dto.setDocumentId("12345678901");
        dto.setAmount("300.00");

        assertThrows(CardNumberNotAllowedException.class, () -> {
            createPaymentService.execute(dto);
        });
    }

    @Test
    void shouldNotCreatePaymentWithNullAmount() {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678901");

        assertThrows(InvalidAmountException.class, () -> {
            createPaymentService.execute(dto);
        });
    }
}
