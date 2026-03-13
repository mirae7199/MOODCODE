package com.devcrew.moodcode.domain.order.repository;

import com.devcrew.moodcode.domain.order.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

}
