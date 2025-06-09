package com.victorpalha.fadesppayment.payments.services;

import com.victorpalha.fadesppayment.payments.entities.payment.dtos.PaymentDTO;
import com.victorpalha.fadesppayment.payments.entities.payment.dtos.PaymentMapper;
import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatePaymentService {
    private final PaymentRepository paymentRepository;

    public CreatePaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentModel execute(PaymentDTO paymentDTO) {
        PaymentModel payment = new PaymentMapper().map(paymentDTO);
        payment = paymentRepository.save(payment);
        return payment;
    }
}
