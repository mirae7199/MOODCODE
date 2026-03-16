package com.devcrew.moodcode.domain.wishlist.service;

import com.devcrew.moodcode.domain.brand.Brand;
import com.devcrew.moodcode.domain.wishlist.Wishlist;
import com.devcrew.moodcode.domain.wishlist.WishlistBrand;
import com.devcrew.moodcode.domain.brand.repository.BrandRepository;
import com.devcrew.moodcode.domain.wishlist.repository.WishlistBrandRepository;
import com.devcrew.moodcode.domain.wishlist.repository.WishlistRepository;
import com.devcrew.moodcode.domain.wishlist.service.response.FindWishlistBrandsResponse;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistBrandService implements WishlistService {

  private final BrandRepository brandRepository;
  private final WishlistRepository wishlistRepository;
  private final WishlistBrandRepository wishlistBrandRepository;

  @Transactional
  @Override
  public void like(Long userId, Long brandId) {
    Wishlist wishlist = findWishlistByUserIdWithThrow(userId);

    // 사용자가 요청한(좋아요) 브랜드 인스턴스
    Brand brand = findBrandByIdWithThrow(brandId);

    List<WishlistBrand> wishlistBrands = wishlist.getWishlistBrands();

    wishlistBrands.add(WishlistBrand.of(brand, wishlist));

    wishlistRepository.save(wishlist);
  }

  @Transactional
  @Override
  public void remove(Long userId, Long brandId) {
    WishlistBrand wishlistBrand = findWishlistBrandByBrandIdWithThrow(brandId);

    wishlistBrandRepository.delete(wishlistBrand);
  }

  @Transactional(readOnly = true)
  @Override
  public FindWishlistBrandsResponse getWishlist(Long userId) {
    // 로그인한 자신의 id로 자신의 위시리스트(좋아요 목록)을 조회
    Wishlist wishlist = findWishlistByUserIdWithThrow(userId);

    // 위시리스트에 "담겨 있는" 브랜드 목록 조회
    List<WishlistBrand> wishlistBrands = wishlist.getWishlistBrands();

    // 브랜드 위시리스트에 담겨 있는 브랜드 객체 추출
    List<Brand> brands = wishlistBrands.stream()
        .map(WishlistBrand::getBrand)
        .toList();

    return FindWishlistBrandsResponse.from(brands);
  }

  private Wishlist findWishlistByUserIdWithThrow(Long userId) {
    return wishlistRepository.findByUserId(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.WISHLIST_NOT_FOUND));
  }

  private WishlistBrand findWishlistBrandByWishlistIdWithThrow(Long wishlistId) {
    return wishlistBrandRepository.findById(wishlistId)
        .orElseThrow(() -> new BusinessException(ErrorCode.WISHLIST_BRAND_NOT_FOUND));
  }

  private Brand findBrandByIdWithThrow(Long brandId) {
    return brandRepository.findById(brandId)
        .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND));
  }

  private WishlistBrand findWishlistBrandByBrandIdWithThrow(Long brandId) {
    WishlistBrand wishlistBrand = wishlistBrandRepository.findByBrandId(brandId)
        .orElseThrow(() -> new BusinessException(ErrorCode.WISHLIST_BRAND_NOT_FOUND));
    return wishlistBrand;
  }
}
