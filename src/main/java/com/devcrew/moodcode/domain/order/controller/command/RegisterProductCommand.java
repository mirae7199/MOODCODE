package com.devcrew.moodcode.domain.order.controller.command;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record RegisterProductCommand(
    Long productOptionId,
    Integer count,
    @Nullable Long addressId
) {

}
