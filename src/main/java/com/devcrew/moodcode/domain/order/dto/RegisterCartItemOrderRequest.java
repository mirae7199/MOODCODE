package com.devcrew.moodcode.domain.order.dto;

import com.devcrew.moodcode.domain.order.controller.command.RegisterCartItemOrderCommand;
import jakarta.annotation.Nullable;
import java.util.List;

public record RegisterCartItemOrderRequest(
    List<Long> cartItemIds,
    @Nullable Long addressId
) {

  public RegisterCartItemOrderCommand toCommand() {
    return RegisterCartItemOrderCommand.builder()
        .cartItemIds(cartItemIds)
        .addressId(addressId)
        .build();
  }


}
