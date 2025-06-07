package com.victorpalha.fadesppayment.payments.entities.payment;

public class PaymentMapper {
    public PaymentModel map(PaymentDTO paymentDTO){
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setPaymentMethod(paymentDTO.getPaymentMethod());
        paymentModel.setDebitCode(paymentDTO.getDebitCode());
        paymentModel.setCreditCardNumber(paymentDTO.getCreditCardNumber());
        paymentModel.setDocumentId(paymentDTO.getDocumentId());
        paymentModel.setAmount(paymentDTO.getAmount());
        return paymentModel;
    }

    public PaymentDTO map(PaymentModel paymentModel){
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(paymentModel.getId());
        paymentDTO.setDebitCode(paymentModel.getDebitCode());
        paymentDTO.setCreditCardNumber(paymentModel.getCreditCardNumber());
        paymentDTO.setDocumentId(paymentModel.getDocumentId());
        paymentDTO.setDocumentType(paymentModel.getDocumentType());
        paymentDTO.setPaymentStatus(paymentModel.getPaymentStatus());
        paymentDTO.setPaymentMethod(paymentModel.getPaymentMethod());
        paymentDTO.setAmount(paymentModel.getAmount());
        paymentDTO.setActive(paymentModel.isActive());
        return paymentDTO;
    }
}
