package com.victorpalha.fadesppayment.payments.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void setup() {
        paymentRepository.deleteAll();
    }

    // Create Payments
    @Test
    public void shouldCreatePaymentSuccessfully() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("300.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.paymentStatus").value("PENDENTE"))
                .andExpect(jsonPath("$.amount").value(300.00));
    }

    @Test
    public void shouldFailWithInvalidDocumentId() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("123");
        dto.setAmount("200.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.documentId").value("Invalid document ID. Must be 11 (CPF) or 14 (CNPJ) digits."));
    }

    @Test
    public void shouldFailWithInvalidCardNumber() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("1111222233334443");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("250.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid card number. Failed Luhn check."));
    }

    @Test
    public void shouldFailWithCardNotAllowedForPaymentType() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.BOLETO);
        dto.setDocumentId("12345678901");
        dto.setAmount("180.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Card number can only be set for CARTAO_CREDITO or CARTAO_DEBITO payment methods."));
    }

    @Test
    public void shouldFailWithNoCardAllowedForPaymentType() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber(null);
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("180.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid card number. Failed Luhn check."));
    }

    @Test
    public void shouldCreatePaymentWithCNPJ() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("12345678000195");
        dto.setAmount("400.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentType").value("CNPJ"))
                .andExpect(jsonPath("$.amount").value(400.00));
    }

    @Test
    public void shouldCreatePaymentWithDebitCard() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_DEBITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("120.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentMethod").value("CARTAO_DEBITO"))
                .andExpect(jsonPath("$.amount").value(120.00));
    }
    // Fetch Payments

    @Test
    public void shouldFetchAllPayments() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(1L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_DEBITO);
        dto.setDocumentId("12345678901");
        dto.setAmount("120.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].debitCode").value(1))
                .andExpect(jsonPath("$[0].documentId").value("12345678901"))
                .andExpect(jsonPath("$[0].amount").value(120.00))
                .andExpect(jsonPath("$[0].paymentMethod").value("CARTAO_DEBITO"));
    }

    @Test
    public void shouldFetchPaymentByDebitCode() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(2L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_DEBITO);
        dto.setDocumentId("12345678902");
        dto.setAmount("200.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/payment?debitCode=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].debitCode").value(2));
    }

    @Test
    public void shouldFetchPaymentByDocumentId() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(3L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_DEBITO);
        dto.setDocumentId("98765432100");
        dto.setAmount("300.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/payment?documentId=98765432100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].documentId").value("98765432100"));
    }

    @Test
    public void shouldFetchPaymentByPaymentStatus() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(4L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_DEBITO);
        dto.setDocumentId("11111111111");
        dto.setAmount("400.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/payment?paymentStatus=PENDENTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.debitCode == 4)]").exists());
    }

    @Test
    public void shouldFetchPaymentByMultipleFilters() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setDebitCode(5L);
        dto.setCreditCardNumber("4539578763621486");
        dto.setPaymentMethod(PaymentType.CARTAO_CREDITO);
        dto.setDocumentId("22222222222");
        dto.setAmount("500.00");

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/payment?debitCode=5&documentId=22222222222&paymentStatus=PENDENTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].debitCode").value(5))
                .andExpect(jsonPath("$[0].documentId").value("22222222222"));
    }
}
