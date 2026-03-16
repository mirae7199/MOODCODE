package com.devcrew.moodcode.domain.product.dto;

import java.util.List;

public record ProductListResponse(
        int totalCount,
        List<ProductResponse> products
) {
}
