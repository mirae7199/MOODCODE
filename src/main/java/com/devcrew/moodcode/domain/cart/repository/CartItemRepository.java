package com.devcrew.moodcode.domain.cart.repository;

import com.devcrew.moodcode.domain.cart.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  Optional<CartItem> findByProductOptionId(Long productOptionId);
  List<CartItem> findByCart_Id(Long cartId);
  boolean existsByCartIdAndProductOptionId(Long cartId, Long productOptionId);
  List<CartItem> findByIdInAndCart_User_Id(List<Long> cartItemIds, Long userId);
  void deleteAllByProductOptionIdIn(List<Long> productOptionIds);
}
