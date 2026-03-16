package com.devcrew.moodcode.domain.auth.controller;

import com.devcrew.moodcode.domain.auth.dto.LoginRequest;
import com.devcrew.moodcode.domain.auth.dto.TokenResponse;
import com.devcrew.moodcode.domain.auth.dto.UserSignupRequest;
import com.devcrew.moodcode.domain.auth.service.AuthService;
import com.devcrew.moodcode.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody @Valid UserSignupRequest req) {
        Long userId = authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    /**
     * 자체 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        TokenResponse tokenResponse = authService.login(req);
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 로그아웃
     * - Access Token을 블랙리스트에 등록하여 무효화
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String bearerToken,
            @LoginUser Long userId // 본인 확인용
    ) {
        // Bearer 제외하고 순수 토큰만 추출
        String accessToken = bearerToken.substring(7);
        authService.logout(accessToken, userId);

        return ResponseEntity.ok().build();
    }
}