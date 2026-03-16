package com.devcrew.moodcode.domain.address.controller;

import com.devcrew.moodcode.domain.address.dto.AddressListResponse;
import com.devcrew.moodcode.domain.address.dto.AddressResponse;
import com.devcrew.moodcode.domain.address.dto.AddressCreateRequest;
import com.devcrew.moodcode.domain.address.service.AddressService;
import com.devcrew.moodcode.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 배송지 추가
     * - @LoginUser: 토큰에서 userId 자동 주입
     * - @Valid: 입력값 검증
     */
    @PostMapping
    public ResponseEntity<Void> addAddress(
            @LoginUser Long userId,
            @RequestBody @Valid AddressCreateRequest req
    ) {
        addressService.addAddress(userId, req.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 내 배송지 목록 조회
     */
    @GetMapping
    public ResponseEntity<AddressListResponse> getMyAddresses(@LoginUser Long userId) {
        AddressListResponse response = addressService.getMyAddresses(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 배송지 삭제
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @LoginUser Long userId,
            @PathVariable Long addressId
    ) {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}