package com.devcrew.moodcode.domain.cart.service.command;

import lombok.Builder;

@Builder
public record AddCartItemCommand(
    Long productOptionId,
    Integer count
) {

}
