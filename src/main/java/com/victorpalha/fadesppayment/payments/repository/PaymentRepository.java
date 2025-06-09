package com.victorpalha.fadesppayment.payments.repository;

import com.victorpalha.fadesppayment.payments.entities.payment.PaymentModel;
import com.victorpalha.fadesppayment.payments.entities.payment.enums.PaymentStatusType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentModel, UUID> {

    @Query("SELECT p FROM PaymentModel p " +
            "WHERE (:debitCode IS NULL OR p.debitCode = :debitCode) " +
            "AND (:documentId IS NULL OR p.documentId = :documentId) " +
            "AND (:status IS NULL OR p.paymentStatus = :status)")
    List<PaymentModel> findByFilters(@Param("debitCode") Long debitCode,
                                     @Param("documentId") String documentId,
                                     @Param("status") PaymentStatusType paymentStatus);
}
