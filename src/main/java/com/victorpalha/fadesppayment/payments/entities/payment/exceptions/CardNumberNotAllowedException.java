package com.victorpalha.fadesppayment.payments.entities.payment.exceptions;

public class CardNumberNotAllowedException extends RuntimeException {
  public CardNumberNotAllowedException() {
    super("Card number can only be set for CARTAO_CREDITO or CARTAO_DEBITO payment methods.");
  }
}
