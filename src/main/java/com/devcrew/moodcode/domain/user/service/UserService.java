package com.devcrew.moodcode.domain.user.service;

import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.dto.UserInfoUpdateRequest;
import com.devcrew.moodcode.domain.user.dto.UserResponse;
import com.devcrew.moodcode.domain.user.repository.UserRepository;
import com.devcrew.moodcode.global.error.ErrorCode;
import com.devcrew.moodcode.global.error.exception.BusinessException;
import com.devcrew.moodcode.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    // 회원 정보 조회
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    // 회원 정보 수정
    @Transactional
    public UserResponse updateMyInfo(Long userId, UserInfoUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getNickname().equals(request.nickname())
            && userRepository.existsByNickname(request.nickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATE);
        }
        user.updateProfile(request.nickname());
        return UserResponse.from(user);
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(Long userId, String password) {
        // 1. 유저를 조회한다. 없을 경우 BusinessException을 거쳐 정해둔 에러 코드로 예외 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // 2. 비밀번호를 검증한다. 입력받은 비밀번호(평문)과 DB상의 비밀번호(암호문)을 비교한다. 동일하게 에러 코드로 예외 처리
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        // 3. redis에서 해당 유저의 리프레시 토큰을 삭제한다.
        redisService.deleteValues("RT:" + userId);
        // 4. DB에서 해당 유저를 삭제한다. 엔티티 설계시 CascadeType.ALL를 걸어두어서 배송지 테이블 등 연관 데이터도 자동 삭제된다.
        userRepository.delete(user);
    }
}
