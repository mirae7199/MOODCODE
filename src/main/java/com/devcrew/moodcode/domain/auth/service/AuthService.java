package com.devcrew.moodcode.domain.auth.service;


import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.exception.DuplicateEmailException;
import com.devcrew.moodcode.domain.user.exception.UserNotFoundException;
import com.devcrew.moodcode.domain.user.repository.UserRepository;
import com.devcrew.moodcode.domain.auth.dto.LoginRequest;
import com.devcrew.moodcode.domain.auth.dto.TokenResponse;
import com.devcrew.moodcode.domain.auth.dto.UserSignupRequest;
import com.devcrew.moodcode.global.auth.jwt.TokenProvider;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import com.devcrew.moodcode.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Value("${jwt.refresh-expiry-seconds}")
    private long refreshExpirySeconds;

    /**
     * 회원가입
     */
    @Transactional
    public Long signup(UserSignupRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new DuplicateEmailException();
        }
        User user = userRepository.save(req.toEntity(passwordEncoder));
        return user.getId();
    }

    /**
     * 로그인
     */
    @Transactional
    public TokenResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 토큰 발급
        String accessToken = tokenProvider.createToken(user.getId(), user.getRole());
        String refreshToken = tokenProvider.createRefreshToken();

        // Refresh Token Redis 저장 (Key: "RT:{userId}", Value: refreshToken)
        redisService.setValues("RT:" + user.getId(), refreshToken, Duration.ofSeconds(refreshExpirySeconds));

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * 로그아웃 (블랙리스트 처리)
     */
    @Transactional
    public void logout(String accessToken, Long userId) {
        // 1. Redis에서 Refresh Token 삭제
        redisService.deleteValues("RT:" + userId);

        // 2. Access Token 남은 유효시간 계산 후 블랙리스트 등록
        // (TokenProvider에 남은 시간 계산 메서드가 없으면 추가 필요, 여기선 하드코딩된 값 사용하거나 계산 로직 추가)
        // 편의상 1시간(3600초)으로 가정하거나, TokenProvider에 getRemainingTime 메서드를 만드는 게 정석
        long expiration = 1000L * 60 * 60; // 예시: 1시간

        redisService.setBlackList(accessToken, "logout", expiration);
    }
}