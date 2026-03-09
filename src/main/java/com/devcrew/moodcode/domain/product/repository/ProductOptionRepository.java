package com.devcrew.moodcode.domain.product.repository;

import com.devcrew.moodcode.domain.product.ProductOption;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProductId(Long productId);
    Optional<ProductOption> findByProductIdAndName(Long productId, String optionName);
}
