package com.devcrew.moodcode.domain.order;

import com.devcrew.moodcode.domain.product.ProductOption;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ordered_product")
public class OrderedProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_product_id")
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, name = "product_name_snapshot")
    private String productNameSnapshot;

    @Column(nullable = false, name = "option_name_snapshot")
    private String optionNameSnapshot;

    @Enumerated(EnumType.STRING)
    private OrderedProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    public static OrderedProduct of(Integer quantity, Integer price, String productNameSnapshot, String optionNameSnapshot,
        OrderedProductStatus status, Orders orders, ProductOption productOption) {
        return OrderedProduct.builder()
            .quantity(quantity)
            .price(price)
            .productNameSnapshot(productNameSnapshot)
            .optionNameSnapshot(optionNameSnapshot)
            .status(status)
            .orders(orders)
            .productOption(productOption)
            .build();
    }
}
