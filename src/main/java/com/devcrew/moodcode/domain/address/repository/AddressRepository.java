package com.devcrew.moodcode.domain.address.repository;

import com.devcrew.moodcode.domain.address.Address;
import com.devcrew.moodcode.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // 특정 유저의 모든 배송지 조회
    List<Address> findAllByUser(User user);

    // 특정 유저의 기본 배송지만 가져와서 기존은 false로
    Optional<Address> findByUserAndIsDefaultTrue(User user);

    // 유저의 배송지 개수 (첫 배송지인지 확인용)
    int countByUser(User user);
}