package com.devcrew.moodcode.domain.wishlist.service;

import com.devcrew.moodcode.domain.product.Product;
import com.devcrew.moodcode.domain.product.repository.ProductRepository;
import com.devcrew.moodcode.domain.wishlist.Wishlist;
import com.devcrew.moodcode.domain.wishlist.WishlistProduct;
import com.devcrew.moodcode.domain.wishlist.repository.WishlistProductRepository;
import com.devcrew.moodcode.domain.wishlist.repository.WishlistRepository;
import com.devcrew.moodcode.domain.wishlist.service.response.FindWishlistProductsResponse;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistProductService implements WishlistService{

  private final ProductRepository productRepository;
  private final WishlistRepository wishlistRepository;
  private final WishlistProductRepository wishlistProductRepository;

  @Transactional
  @Override
  public void like(Long userId, Long productId) {
    Wishlist wishlist = findWishlistByUserIdWithThrow(userId);
    List<WishlistProduct> wishlistProducts = wishlist.getWishlistProducts();

    // 사용자가 "좋아요" 요청한 상품 인스턴스
    Product product = findProductByIdWithThrow(productId);

    wishlistProducts.add(WishlistProduct.of(product, wishlist));

    wishlistRepository.save(wishlist);
  }


  @Transactional
  @Override
  public void remove(Long userId, Long productId) {
    // 사용자가 "삭제" 요청한 상품 인스턴스
    Product product = findProductByIdWithThrow(productId);

    WishlistProduct wishlistProduct = findWishlistProductByProductIdWithThrow(product);

    wishlistProductRepository.delete(wishlistProduct); // 연관된 레코드 싹다 삭제됨. cascade
  }

  @Transactional(readOnly = true)
  @Override
  public FindWishlistProductsResponse getWishlist(Long userId) {
    // 로그인한 자신의 id로 자신의 위시리스트(좋아요 목록)을 조회
    Wishlist wishlist = findWishlistByUserIdWithThrow(userId);

    // 위시리스트에 "담겨 있는" 상품 목록 조회
    List<WishlistProduct> wishlistProducts = wishlist.getWishlistProducts();

    // wishlistProducts에 "담겨 있는" 상품의 객체만 추출
    List<Product> products = wishlistProducts.stream()
        .map(WishlistProduct::getProduct) // wishlistProduct.getProduct() 임.
        .toList();

    return FindWishlistProductsResponse.from(products);
  }

  private List<WishlistProduct> findAllByWishlistId(Long wishlistId) {
    return wishlistProductRepository.findAllByWishlistId(
        wishlistId);
  }

  private Wishlist findWishlistByUserIdWithThrow(Long userId) {
    return wishlistRepository.findByUserId(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.WISHLIST_NOT_FOUND));
  }

  private Product findProductByIdWithThrow(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
  }

  private WishlistProduct findWishlistProductByProductIdWithThrow(Product product) {
    WishlistProduct wishlistProduct = wishlistProductRepository.findByProductId(product.getId())
        .orElseThrow(() -> new BusinessException(ErrorCode.WISHLIST_PRODUCT_NOT_FOUND));
    return wishlistProduct;
  }
}

