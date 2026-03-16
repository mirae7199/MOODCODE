package com.devcrew.moodcode.domain.order.repository;

import com.devcrew.moodcode.domain.order.Orders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

  List<Orders> findByUser_id(Long userId);

  Optional<Orders> findByUser_idAndOrderNo(Long userId, String orderNo);
}
