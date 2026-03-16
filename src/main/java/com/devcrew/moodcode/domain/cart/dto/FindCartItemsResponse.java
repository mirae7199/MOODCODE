package com.devcrew.moodcode.domain.cart.dto;

import com.devcrew.moodcode.domain.cart.CartItem;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record FindCartItemsResponse(
    List<FindCartItemResponse> findCartItemResponse,
    Integer totalPrice
) {

  public static FindCartItemsResponse from(
      List<CartItem> cartItems) {
    int total = cartItems.stream()
            .mapToInt(item -> item.getCount() * item.getProductOption().getProduct().getOriginalPrice())
            .sum();

    return new FindCartItemsResponse(
        cartItems.stream().map(cartItem -> {
          var product = cartItem.getProductOption().getProduct();
          var brand = product.getBrand() != null ? product.getBrand().getName() : product.getCategory().name();
          return FindCartItemResponse.builder()
              .cartItemId(cartItem.getId())
              .optionName(cartItem.getProductOption().getName())
              .count(cartItem.getCount())
              .updatedAt(cartItem.getUpdatedAt())
              .createdAt(cartItem.getCreatedAt())
              .productOptionId(cartItem.getProductOption().getId())
              .cartId(cartItem.getCart().getId())
              .productId(product.getId())
              .productName(product.getName())
              .price(product.getOriginalPrice())
              .imageUrl(product.getThumbnailImageUrl())
              .brand(brand)
              .build();
        }).toList(),
        total
    );
  }
  @Builder
  public record FindCartItemResponse(
      Long cartItemId,
      String optionName,
      Integer count,
      LocalDateTime updatedAt,
      LocalDateTime createdAt,
      Long productOptionId,
      Long cartId,
      Long productId,
      String productName,
      Integer price,
      String imageUrl,
      String brand
  ) {

  }
}


