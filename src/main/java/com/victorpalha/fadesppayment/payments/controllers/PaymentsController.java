package com.victorpalha.fadesppayment.payments.controllers;

import com.victorpalha.fadesppayment.payments.entities.payment.dtos.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.dtos.PaymentMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.dtos.UpdatePaymentStatusDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.services.CreatePaymentService;
import com.victorpalha.fadesppayment.payments.services.FetchPaymentsService;
import com.victorpalha.fadesppayment.payments.services.UpdatePaymentStatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaymentsController {

    private final CreatePaymentService createPaymentService;
    private final FetchPaymentsService fetchPaymentsService;
    private final UpdatePaymentStatusService updatePaymentStatusService;

    public PaymentsController(CreatePaymentService createPaymentService, FetchPaymentsService fetchPaymentsService, UpdatePaymentStatusService updatePaymentStatusService) {
        this.createPaymentService = createPaymentService;
        this.fetchPaymentsService = fetchPaymentsService;
        this.updatePaymentStatusService = updatePaymentStatusService;
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

    @PatchMapping("/payment/{id}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePaymentStatusDTO dto
    ) {
        PaymentModel paymentUpdated = updatePaymentStatusService.execute(id, dto.getStatus());
        return ResponseEntity.ok(new PaymentMapper().map(paymentUpdated));
    }
}
