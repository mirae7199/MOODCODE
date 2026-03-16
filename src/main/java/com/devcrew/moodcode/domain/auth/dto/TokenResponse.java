package com.devcrew.moodcode.domain.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}