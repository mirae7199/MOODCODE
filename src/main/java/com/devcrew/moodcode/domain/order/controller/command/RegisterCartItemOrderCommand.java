package com.devcrew.moodcode.domain.order.controller.command;

import jakarta.annotation.Nullable;
import java.util.List;
import lombok.Builder;

@Builder
public record RegisterCartItemOrderCommand(
    List<Long> cartItemIds,
    @Nullable Long addressId
) {

}
