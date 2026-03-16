package com.devcrew.moodcode.domain.payment.controller;

import com.devcrew.moodcode.domain.payment.dto.ConfirmPaymentRequest;
import com.devcrew.moodcode.domain.payment.service.PaymentService;
import com.devcrew.moodcode.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping()
  public void confirmPayment(
      @LoginUser Long userId,
      ConfirmPaymentRequest request
  ) {

    paymentService.confirmPayment(userId, request.toCommand());

  }

}
