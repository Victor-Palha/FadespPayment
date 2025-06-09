package com.victorpalha.fadesppayment.payments.services;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.PaymentNotFoundException;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdatePaymentStatusService {
    private final PaymentRepository paymentRepository;

    public UpdatePaymentStatusService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentModel execute(String paymentId, PaymentStatusType paymentStatusType) {
        PaymentModel payment = paymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.setPaymentStatus(paymentStatusType);
        return paymentRepository.save(payment);
    }
}
