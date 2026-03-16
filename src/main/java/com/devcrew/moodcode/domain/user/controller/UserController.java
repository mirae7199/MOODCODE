package com.devcrew.moodcode.domain.user.controller;

import com.devcrew.moodcode.domain.user.dto.UserInfoUpdateRequest;
import com.devcrew.moodcode.domain.user.dto.UserResponse;
import com.devcrew.moodcode.domain.user.dto.UserWithdrawRequest;
import com.devcrew.moodcode.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(
            @AuthenticationPrincipal Long userId) {
     // Long userId = Long.parseLong(userDetails.getUsername());
        UserResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserInfoUpdateRequest request) {
     // Long userId = Long.parseLong(userDetails.getUsername());
        UserResponse response = userService.updateMyInfo(userId, request);
        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserWithdrawRequest request
    ) {
        // Long userId = Long.parseLong(userDetails.getUsername());
        // 서비스(비즈니스) 로직 호출
        userService.withdraw(userId, request.password());
        // 성공 시 204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}
