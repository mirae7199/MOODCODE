package com.devcrew.moodcode.domain.order.dto;

import com.devcrew.moodcode.domain.order.controller.command.RegisterProductCommand;
import jakarta.annotation.Nullable;

public record RegisterProductRequest(
    Long productOptionId,
    Integer count,
    @Nullable Long addressId
) {

  public RegisterProductCommand toCommand() {
    return RegisterProductCommand.builder()
        .productOptionId(productOptionId)
        .count(count)
        .addressId(addressId)
        .build();
  }

}
