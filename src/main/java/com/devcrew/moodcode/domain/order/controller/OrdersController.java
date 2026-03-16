package com.devcrew.moodcode.domain.order.controller;

import com.devcrew.moodcode.domain.order.dto.FindOrderDetailResponse;
import com.devcrew.moodcode.domain.order.dto.FindOrdersResponse;
import com.devcrew.moodcode.domain.order.dto.RegisterCartItemOrderRequest;
import com.devcrew.moodcode.domain.order.dto.RegisterProductRequest;
import com.devcrew.moodcode.domain.order.service.OrderService;
import com.devcrew.moodcode.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrdersController {

  private final OrderService orderService;

  @PostMapping("/cart")
  public ResponseEntity<String> cartItemOrder(
      @LoginUser Long userId,
      @RequestBody @Valid RegisterCartItemOrderRequest request
  ) {
    String orderNo = orderService.createCartItemOrder(
        userId,
        request.toCommand()
    );

    return ResponseEntity.ok(orderNo);

  }

  @PostMapping("/product")
  public ResponseEntity<String> productOrder(
      @LoginUser Long userId,
      @RequestBody @Valid RegisterProductRequest request
  ) {
    String orderNo = orderService.createProductOrder(
        userId,
        request.toCommand()
    );

    return ResponseEntity.ok(orderNo);

  }

  @GetMapping
  public ResponseEntity<FindOrdersResponse> getOrders(
      @LoginUser Long userId
  ) {
    FindOrdersResponse response = orderService.getOrders(userId);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{orderNo}")
  public ResponseEntity<FindOrderDetailResponse> getOrdersDetail(
      @LoginUser Long userId,
      @PathVariable String orderNo
  ) {
    FindOrderDetailResponse response = orderService.getOrdersDetail(userId, orderNo);

    return ResponseEntity.ok(response);

  }
}

