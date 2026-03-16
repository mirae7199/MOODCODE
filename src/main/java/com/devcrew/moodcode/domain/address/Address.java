package com.devcrew.moodcode.domain.address;

import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    // 이 부분 중요한데, N+1 문제 방지(동시성)를 위해 LAZY 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String recipientName;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;

    private boolean isDefault; // 기본 배송지 여부

    @Builder
    public Address(User user, String recipientName, String phoneNumber, String roadAddress, String detailAddress, boolean isDefault) {
        this.user = user;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }

    public void changeToDefault() {
        this.isDefault = true;
    }

    public void changeToNormal() {
        this.isDefault = false;
    }
}