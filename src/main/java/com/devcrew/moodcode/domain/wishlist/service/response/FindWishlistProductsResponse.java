package com.devcrew.moodcode.domain.wishlist.service.response;

import com.devcrew.moodcode.domain.product.Product;
import java.util.List;
import lombok.Builder;

@Builder
public record FindWishlistProductsResponse(
    List<FindWishlistProductResponse> responses
) {

  public static FindWishlistProductsResponse from(List<Product> products) {
    List<FindWishlistProductResponse> responses =
        products.stream().map(product -> {
      return FindWishlistProductResponse.builder()
          .productId(product.getId())
          .brandName(product.getBrand().getName())
          .productName(product.getName())
          .originalPrice(product.getOriginalPrice())
          .productLikeCount(product.getLikeCount())
          .build();
    }).toList();

    return FindWishlistProductsResponse.builder()
        .responses(responses)
        .build();

  }

  @Builder
  record FindWishlistProductResponse(Long productId, String brandName, String productName, Integer originalPrice, Integer productLikeCount) {}
}
