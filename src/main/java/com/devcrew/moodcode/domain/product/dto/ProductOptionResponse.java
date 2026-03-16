package com.devcrew.moodcode.domain.product.dto;

import com.devcrew.moodcode.domain.product.ProductOption;
import lombok.Builder;

@Builder
public record ProductOptionResponse(
        Long productOptionId,
        String optionName,
        Integer stock
) {
    public static ProductOptionResponse from(ProductOption option) {
        return ProductOptionResponse.builder()
                .productOptionId(option.getId())
                .optionName(option.getName())
                .stock(option.getStock())
                .build();
    }
}