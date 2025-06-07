package com.victorpalha.fadesppayment.payments.repository;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> { }
