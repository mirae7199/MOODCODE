package com.devcrew.moodcode.domain.payment.dto;

import com.devcrew.moodcode.domain.payment.controller.command.ConfirmPaymentCommand;
import lombok.Builder;

@Builder
public record ConfirmPaymentRequest(
    String paymentKey,
    String orderId,
    Integer amount
) {

  public ConfirmPaymentCommand toCommand() {
    return ConfirmPaymentCommand.builder()
        .paymentKey(paymentKey)
        .orderId(orderId)
        .amount(amount)
        .build();
  }
}
