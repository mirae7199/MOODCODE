package com.devcrew.moodcode.domain.wishlist.service.response;

import com.devcrew.moodcode.domain.brand.Brand;
import java.util.List;
import lombok.Builder;

@Builder
public record FindWishlistBrandsResponse(List<FindWishlistBrandResponse> responses) {

  public static FindWishlistBrandsResponse from(
      List<Brand> brands) {
    List<FindWishlistBrandResponse> responses = brands.stream().map(brand -> {
      return FindWishlistBrandResponse.builder()
          .brandId(brand.getId())
          .brandName(brand.getName())
          .brandLikeCount(brand.getLikeCount())
          .build();
    }).toList();

    return FindWishlistBrandsResponse.builder()
        .responses(responses)
        .build();

  }

  @Builder
  public static record FindWishlistBrandResponse(Long brandId, String brandName, int brandLikeCount) {}

}
