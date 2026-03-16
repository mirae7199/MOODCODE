package com.devcrew.moodcode.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInfoUpdateRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min=2, max=10, message = "닉네임은 최소 2~10자여야 합니다.")
        String nickname
) {

}
