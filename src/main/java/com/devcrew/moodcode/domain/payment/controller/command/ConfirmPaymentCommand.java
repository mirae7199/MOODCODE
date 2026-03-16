package com.devcrew.moodcode.domain.payment.controller.command;

import lombok.Builder;

@Builder
public record ConfirmPaymentCommand(
    String paymentKey,
    String orderId,
    Integer amount
) {

}
