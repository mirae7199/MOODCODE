package com.devcrew.moodcode.domain.address.service;

import com.devcrew.moodcode.domain.address.Address;
import com.devcrew.moodcode.domain.address.dto.AddressListResponse;
import com.devcrew.moodcode.domain.address.dto.AddressResponse;
import com.devcrew.moodcode.domain.address.repository.AddressRepository;
import com.devcrew.moodcode.domain.address.service.command.AddressCreateCommand;
import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.exception.UserNotFoundException;
import com.devcrew.moodcode.domain.user.repository.UserRepository;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    /**
     * 배송지 추가
     */
    @Transactional
    public void addAddress(Long userId, AddressCreateCommand command) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 1. 첫 배송지면 무조건 기본 배송지로 설정
        boolean isFirst = addressRepository.countByUser(user) == 0;
        boolean shouldBeDefault = command.isDefault() || isFirst;

        // 2. 기본 배송지로 설정해야 한다면, 기존 기본 배송지 해제
        if (shouldBeDefault) {
            addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(Address::changeToNormal);
        }

        // 3. 저장
        Address address = command.toEntity(user);
        if (shouldBeDefault) address.changeToDefault();

        addressRepository.save(address);
    }

    /**
     * 배송지 목록 조회
     */
    public AddressListResponse getMyAddresses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<Address> addresses = addressRepository.findAllByUser(user);
        return AddressListResponse.from(addresses);
    }

    /**
     * 배송지 삭제
     */
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE)); // AddressNotFound 필요

        // 본인 배송지인지 확인
        if (!address.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        addressRepository.delete(address);
    }
}