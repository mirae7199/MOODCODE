package com.devcrew.moodcode.domain.payment;

import com.devcrew.moodcode.domain.order.Orders;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
  @Id @Column(name = "payment_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 필수 3개
  private String paymentKey; // 토스가 발급한 결제 고유 키
  private Integer amount; // 실제 결제된 금액
  private String orderId; // orderNo (주문 번호)

  @Enumerated(value = EnumType.STRING)
  private PaymentMethod method;

  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status;

  @Column(name = "request_at")
  private LocalDateTime requestedAt; // 결제 요청 시각

  @Column(name = "approved_at")
  private LocalDateTime approvedAt; // 결제 승인 시각

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orders_id")
  private Orders orders;

}
