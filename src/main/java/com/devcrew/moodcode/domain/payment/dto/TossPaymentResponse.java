package com.devcrew.moodcode.domain.payment.dto;

import com.devcrew.moodcode.domain.payment.Payment;
import com.devcrew.moodcode.domain.payment.PaymentMethod;
import com.devcrew.moodcode.domain.payment.PaymentStatus;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record TossPaymentResponse(
  String paymentKey,
  String orderId,
  String method,
  Integer totalAmount,
  String status,
  String requestedAt,
  String approvedAt
) {

  public Payment toEntity() {
    return Payment.builder()
        .paymentKey(paymentKey)
        .orderId(orderId)
        .method(PaymentMethod.valueOf(method))
        .amount(totalAmount)
        .status(PaymentStatus.valueOf(status))
        .requestedAt(OffsetDateTime.parse(requestedAt).toLocalDateTime())
        .approvedAt(OffsetDateTime.parse(approvedAt).toLocalDateTime())
        .build();
  }

}
