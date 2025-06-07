package com.victorpalha.fadesppayment.payments.controllers;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.services.CreatePaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentsController {

    private final CreatePaymentService createPaymentService;

    public PaymentsController(CreatePaymentService createPaymentService) {
        this.createPaymentService = createPaymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody @Valid PaymentDTO paymentDTO) {
        PaymentModel paymentModel = createPaymentService.execute(paymentDTO);
        return ResponseEntity.ok(new PaymentMapper().map(paymentModel));
    }
}
