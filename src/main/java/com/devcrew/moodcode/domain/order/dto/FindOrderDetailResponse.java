package com.devcrew.moodcode.domain.order.dto;

import com.devcrew.moodcode.domain.order.Orders;
import lombok.Builder;

@Builder
public record FindOrderDetailResponse(
    Orders orders
) {

  public static FindOrderDetailResponse from(
      Orders orders
  ) {
    return FindOrderDetailResponse.builder()
        .orders(orders)
        .build();
  }


}
