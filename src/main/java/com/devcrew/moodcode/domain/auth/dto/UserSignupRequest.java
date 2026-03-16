package com.devcrew.moodcode.domain.auth.dto;

import com.devcrew.moodcode.domain.user.Provider;
import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserSignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 8자 이상, 영문/숫자/특수문자를 포함해야 합니다.")
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname
) {
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(UserRole.USER) // 기본 권한 USER
                .provider(Provider.LOCAL)
                .build();
    }
}