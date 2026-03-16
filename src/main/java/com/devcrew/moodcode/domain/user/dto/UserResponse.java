package com.devcrew.moodcode.domain.user.dto;

import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.UserRole;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String email,
        String nickname,
        UserRole userRole
) {
    public static UserResponse from (User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userRole(user.getRole())
                .build();
    }
}