package com.devcrew.moodcode.domain.order.dto;

import com.devcrew.moodcode.domain.order.OrderStatus;
import com.devcrew.moodcode.domain.order.Orders;
import java.util.List;
import lombok.Builder;

@Builder
public record FindOrdersResponse(
    List<FindOrderResponse> responses
) {

  public static FindOrdersResponse from(
    List<Orders> orders
  ) {
    List<FindOrderResponse> responses = orders.stream()
        .map(order -> {
          return FindOrderResponse.builder()
              .orderNo(order.getOrderNo())
              .totalPrice(order.getPrice())
              .status(order.getStatus())
              .build();
        }).toList();

    return FindOrdersResponse.builder()
        .responses(responses)
        .build();
  }


  @Builder
  public record FindOrderResponse(
      String orderNo,
      Integer totalPrice,
      OrderStatus status
  ) {

  }
}


