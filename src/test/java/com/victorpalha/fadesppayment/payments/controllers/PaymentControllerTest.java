package com.victorpalha.fadesppayment.payments.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(status().isOk())
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
                .andExpect(status().isOk())
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethod").value("CARTAO_DEBITO"))
                .andExpect(jsonPath("$.amount").value(120.00));
    }
}
