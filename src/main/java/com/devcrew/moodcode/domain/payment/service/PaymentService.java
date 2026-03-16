package com.devcrew.moodcode.domain.payment.service;

import com.devcrew.moodcode.domain.cart.repository.CartItemRepository;
import com.devcrew.moodcode.domain.order.OrderStatus;
import com.devcrew.moodcode.domain.order.Orders;
import com.devcrew.moodcode.domain.order.repository.OrdersRepository;
import com.devcrew.moodcode.domain.payment.Payment;
import com.devcrew.moodcode.domain.payment.controller.command.ConfirmPaymentCommand;
import com.devcrew.moodcode.domain.payment.dto.TossPaymentResponse;
import com.devcrew.moodcode.domain.payment.repository.PaymentRepository;
import com.devcrew.moodcode.domain.product.ProductOption;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import jakarta.persistence.OptimisticLockException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

  @Value("${tosspayments.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final PaymentRepository paymentRepository;
  private final OrdersRepository ordersRepository;
  private final CartItemRepository cartItemRepository;

  @Transactional(readOnly = false)
  public void confirmPayment(Long userId, ConfirmPaymentCommand command) {

    Orders orders = ordersRepository.findByUser_idAndOrderNo(userId, command.orderId())
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDERS_NOT_FOUND));

    // 금액 검증 (DB의 price == amount)
    if (!orders.getPrice().equals(command.amount())) {
      throw new BusinessException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
    }

    TossPaymentResponse tossPaymentResponse = callTossConfirmApi(command);

    Payment payment = tossPaymentResponse.toEntity();
    payment.setOrders(orders); // 수정

    paymentRepository.save(payment);

    // 재고 차감 (낙관적 락)
    try {
      orders.getOrderedProducts().forEach(orderedProduct -> {
        ProductOption productOption = orderedProduct.getProductOption();
        productOption.decreaseStock(orderedProduct.getQuantity());
      });
    } catch (OptimisticLockException e) {
      throw new BusinessException(ErrorCode.OUT_OF_STOCK); // SOLD OUT
    }

    List<Long> productOptionIds = orders.getOrderedProducts().stream().map(orderedProduct -> {
      return orderedProduct.getProductOption().getId();
    }).toList();

    // 사용자가 장바구니에서 주문한 상품 삭제 (cartItem 삭제)
    cartItemRepository.deleteAllByProductOptionIdIn(productOptionIds);

    orders.updateStatus(OrderStatus.PAID);
  }

  /**
   * @param command
   * @return TossPaymentResponse
   * WebClient를 사용하여 토스 서버에 결제 요청 보내기.
   */
  private TossPaymentResponse callTossConfirmApi(ConfirmPaymentCommand command) {
    // Basic Base 64 인코딩 (secretKey: 비밀번호 없음)
    String encoded = Base64.getEncoder()
        .encodeToString((secretKey + ":").getBytes());

    try {
      return webClient.post() // post 요청
          .uri("https://api.tosspayments.com/v1/payments/confirm")
          .header("Authorization", "Basic " + encoded) // 인증 헤더
          .header("Content-Type", "application/json")
          .bodyValue(Map.of( // body에 보낼 데이터
              "paymentKey", command.paymentKey(),
              "orderId", command.orderId(),
              "amount", command.amount()
          ))
          .retrieve()
          .bodyToMono(TossPaymentResponse.class) // 응답을 DTO로 변환
          .block();
    } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
      System.err.println("Toss Error Response: " + e.getResponseBodyAsString());
      throw e;
    }
  }

}

