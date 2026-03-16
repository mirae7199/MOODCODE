package com.devcrew.moodcode.domain.user;

import com.devcrew.moodcode.domain.address.Address;
import com.devcrew.moodcode.domain.order.Orders;
import com.devcrew.moodcode.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users") // 🚨 필수: DB 예약어 'USER' 충돌 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 프록시용 빈 생성자
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password; // 소셜 로그인 회원은 비밀번호가 없을 수 있음

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private Provider provider; // LOCAL, GOOGLE, KAKAO

    private String providerId; // 소셜 로그인 식별자 (sub, id 등)

    // User가 삭제되면 배송지도 같이 삭제 (Cascade)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, UserRole role, Provider provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    // --- [비즈니스 로직] ---

    // 회원 정보 수정 (Dirty Checking)
    public void updateProfile(String newNickname) {
        if (newNickname != null && !newNickname.isBlank()) {
            this.nickname = newNickname;
        }
    }
}