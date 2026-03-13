package com.devcrew.moodcode.domain.order;

import com.devcrew.moodcode.domain.payment.Payment;
import com.devcrew.moodcode.domain.user.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id @Column(name = "orders_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", length = 50, unique = true)
    private String orderNo;

    @Column(name = "receiver_name", length = 20, nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", length = 15, nullable = false)
    private String receiverPhone;

    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private Payment payment;

    public static Orders of(String orderNo, String receiverName, String receiverPhone, String receiverAddress, Integer price, OrderStatus status, User user) {
        return Orders.builder()
            .orderNo(orderNo)
            .receiverName(receiverName)
            .receiverPhone(receiverPhone)
            .receiverAddress(receiverAddress)
            .price(price)
            .status(status)
            .user(user)
            .build();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
