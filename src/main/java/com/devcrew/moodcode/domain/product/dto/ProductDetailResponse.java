package com.devcrew.moodcode.domain.product.dto;

import com.devcrew.moodcode.domain.product.Category;
import com.devcrew.moodcode.domain.product.Product;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductDetailResponse(
        Long productId,
        String productName,
        Category category,
        Integer originalPrice,
        String thumbnailImageUrl,
        List<ProductOptionResponse> options
) {
    public static ProductDetailResponse from(
            Product product,
            List<ProductOptionResponse> options
    ) {
        return ProductDetailResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .category(product.getCategory())
                .originalPrice(product.getOriginalPrice())
                .thumbnailImageUrl(product.getThumbnailImageUrl())
                .options(options)
                .build();
    }
}
