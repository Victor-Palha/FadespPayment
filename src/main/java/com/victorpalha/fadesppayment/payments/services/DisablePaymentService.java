package com.victorpalha.fadesppayment.payments.services;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.exceptions.PaymentNotFoundException;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DisablePaymentService {
    private final PaymentRepository paymentRepository;
    public DisablePaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentModel execute(UUID paymentId){
        PaymentModel payment = paymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotFoundException::new);

        payment.setActive(false);
        return paymentRepository.save(payment);
    }
}
