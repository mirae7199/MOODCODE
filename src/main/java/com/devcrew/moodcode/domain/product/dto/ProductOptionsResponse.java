package com.devcrew.moodcode.domain.product.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ProductOptionsResponse(
    List<ProductOptionResponse> responses
) {

  public static ProductOptionsResponse from(List<ProductOptionResponse> responses) {
    return ProductOptionsResponse.builder()
        .responses(responses)
        .build();
  }
}