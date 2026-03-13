package com.devcrew.moodcode.domain.cart.service;

import com.devcrew.moodcode.domain.cart.Cart;
import com.devcrew.moodcode.domain.cart.CartItem;
import com.devcrew.moodcode.domain.cart.service.command.AddCartItemCommand;
import com.devcrew.moodcode.domain.cart.service.command.UpdateCartItemCommand;
import com.devcrew.moodcode.domain.cart.repository.CartItemRepository;
import com.devcrew.moodcode.domain.cart.repository.CartRepository;
import com.devcrew.moodcode.domain.cart.dto.FindCartItemsResponse;
import com.devcrew.moodcode.domain.product.ProductOption;
import com.devcrew.moodcode.domain.product.repository.ProductOptionRepository;
import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.repository.UserRepository;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductOptionRepository productOptionRepository;

  // 장바구니 상품 추가 기능
  @Transactional
  public void addCartItem(Long userId, AddCartItemCommand command) {
    Cart cart = findCartByUserId(userId); // 장바구니
    List<CartItem> cartItems = cart.getCartItems(); // 장바구니에 담을 상품 배열들

    ProductOption productOption = findProductOption(command.productOptionId()); // 장바구니에 담을 상품 옵션

    if (increaseCountIfDuplicate(cart.getId(), command.productOptionId(), command.count())) {
      return;
    }

    cartItems.add(CartItem.of(productOption, command.count(), cart));
    cartRepository.save(cart);
  }


  // 장바구니 상품 조회 기능
  public FindCartItemsResponse getCartItems(Long userId) {
    Cart cart = findCartByUserId(userId);
    List<CartItem> cartItems = cart.getCartItems();
    return FindCartItemsResponse.from(cartItems);
  }

  // 장바구니 상품 옵션 변경
  @Transactional
  public void updateCartItem(Long userId, Long cartItemId, UpdateCartItemCommand command) {
    CartItem cartItem = findCartItemById(cartItemId);

    Long productId = cartItem.getProductOption().getProduct().getId();
    String optionName = command.optionName();

    ProductOption productOption = findByProductIdAndOptionName(productId, optionName);

    cartItem.updateOption(productOption, command.count());
    cartItemRepository.save(cartItem);
  }

  // 장바구니 상품 삭제 기능
  @Transactional
  public void removeCartItem(Long userId, Long cartItemId) {
    CartItem cartItem = findCartItemById(cartItemId);

    cartItemRepository.delete(cartItem);
  }

  // 같은 상품의 다른 옵션 조회 (옵션을 변경 하려면, 다른 옵션도 조회를 해야 함) 상품 서비스 역할인듯.
//  public FindOptionResponse getOption(Long productId) {
//    List<ProductOption> productOptions = findAllByProductId(productId);
//
//    return FindOptionResponse.
//  }

  private Cart findCartByUserId(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    Cart cart = cartRepository.findByUserId(userId)
        .orElseGet(() -> cartRepository.save(Cart.of(user)));

    return cart;
  }

  private ProductOption findByProductIdAndOptionName(Long productId, String optionName) {
    return productOptionRepository.findByProductIdAndName(productId, optionName)
        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_OPTION));
  }

  private ProductOption findProductOption(Long productOptionId) {
    return productOptionRepository.findById(productOptionId)
        .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
  }

  private CartItem findCartItemById(Long cartItemId) {
    return cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
  }

  private CartItem findCartItemByPrdOId(Long productOptionId) {
    return cartItemRepository.findByProductOptionId(productOptionId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
  }

  private List<ProductOption> findByProductId(Long productId) {
    return productOptionRepository.findByProductId(productId);
  }

  private boolean increaseCountIfDuplicate(Long cartId, Long productOptionId, int count) {
    Boolean isDuplicate = cartItemRepository.existsByCartIdAndProductOptionId(cartId, productOptionId);

    if (isDuplicate) {
      CartItem cartItem = findCartItemByPrdOId(productOptionId);
      cartItem.addCount(count);
      cartItemRepository.save(cartItem);
      return true;
    }
    return false;
  }
}
