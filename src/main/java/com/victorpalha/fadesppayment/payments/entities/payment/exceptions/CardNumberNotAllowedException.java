package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class CardNumberNotAllowedException extends RuntimeException {
  public CardNumberNotAllowedException() {
    super("Card number can only be set for CREDIT_CARD or DEBIT_CARD payment methods.");
  }
}
