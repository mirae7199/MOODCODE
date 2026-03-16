package com.devcrew.moodcode.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 데이터 저장 (Refresh Token 등)
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    // 데이터 조회
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        Object value = values.get(key);
        return value == null ? "false" : (String) value;
    }

    // 데이터 삭제
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    // 블랙리스트 등록 (로그아웃 시 사용)
    public void setBlackList(String key, String data, Long milliSeconds) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, Duration.ofMillis(milliSeconds));
    }

    // 키 존재 여부 확인 (토큰 검증 시 사용)
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}