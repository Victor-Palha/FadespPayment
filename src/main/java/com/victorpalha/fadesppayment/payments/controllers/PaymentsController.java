package com.victorpalha.fadesppayment.payments.controllers;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.services.CreatePaymentService;
import com.victorpalha.fadesppayment.payments.services.FetchPaymentsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentsController {

    private final CreatePaymentService createPaymentService;
    private final FetchPaymentsService fetchPaymentsService;

    public PaymentsController(CreatePaymentService createPaymentService, FetchPaymentsService fetchPaymentsService) {
        this.createPaymentService = createPaymentService;
        this.fetchPaymentsService = fetchPaymentsService;
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody @Valid PaymentDTO paymentDTO) {
        PaymentModel paymentModel = createPaymentService.execute(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new PaymentMapper().map(paymentModel)
        );
    }

    @GetMapping("/payment")
    public ResponseEntity<List<PaymentModel>> listPayments(
            @RequestParam(required = false) Long debitCode,
            @RequestParam(required = false) String documentId,
            @RequestParam(required = false) PaymentStatusType paymentStatus
    ) {
        List<PaymentModel> payments = fetchPaymentsService.execute(debitCode, documentId, paymentStatus);
        return ResponseEntity.ok(payments);
    }
}
