package com.devcrew.moodcode.domain.cart.controller;

import com.devcrew.moodcode.domain.cart.dto.AddCartItemRequest;
import com.devcrew.moodcode.domain.cart.dto.UpdateCartItemRequest;
import com.devcrew.moodcode.domain.cart.service.CartService;
import com.devcrew.moodcode.domain.cart.dto.FindCartItemsResponse;
import com.devcrew.moodcode.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts") // 웹 페이지 url
public class CartController { // controller -> service -> repository -> entity
  private final CartService cartService;

  @PostMapping("/items")
  public ResponseEntity<Void> addCartItem(
      @LoginUser Long userId,
      @RequestBody @Valid AddCartItemRequest request) {

    cartService.addCartItem(userId, request.toCommand());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/items")
  public ResponseEntity<FindCartItemsResponse> getCartItems(
      @LoginUser Long userId
  ) {

    return ResponseEntity.ok(cartService.getCartItems(userId));
  }

  @PatchMapping("/items/{cartItemId}") // /api/v1/cart/items/3
  public ResponseEntity<Void> updateCartItem(
      @LoginUser Long userId,
      @PathVariable Long cartItemId,
      @RequestBody @Valid UpdateCartItemRequest request) {

    cartService.updateCartItem(userId, cartItemId, request.toCommand());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/items/{cartItemId}")
  public ResponseEntity<Void> removeCartItem(
      @LoginUser Long userId,
      @PathVariable Long cartItemId) {

    cartService.removeCartItem(userId, cartItemId);
    return ResponseEntity.noContent().build();
  }

}
