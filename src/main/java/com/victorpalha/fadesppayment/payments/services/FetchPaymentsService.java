package com.victorpalha.fadesppayment.payments.services;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import com.victorpalha.fadesppayment.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchPaymentsService {
    private final PaymentRepository paymentRepository;

    public FetchPaymentsService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentModel> execute(Long debitCode, String documentId, PaymentStatusType paymentStatus){
        return paymentRepository.findByFilters(debitCode, documentId, paymentStatus);
    }
}
