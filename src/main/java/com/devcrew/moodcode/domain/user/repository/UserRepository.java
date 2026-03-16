package com.devcrew.moodcode.domain.user.repository;

import com.devcrew.moodcode.domain.user.Provider;
import com.devcrew.moodcode.domain.user.User;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 회원 찾기 (로그인 시 사용)
    Optional<User> findByEmail(String email);

    // 이메일 중복 검사 (회원가입 시 사용)
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    // 소셜 로그인 회원 찾기 (provider + providerId 조합)
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

}