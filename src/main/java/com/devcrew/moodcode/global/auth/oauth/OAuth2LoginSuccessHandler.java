package com.devcrew.moodcode.global.auth.oauth;


import com.devcrew.moodcode.domain.user.User;
import com.devcrew.moodcode.domain.user.repository.UserRepository;
import com.devcrew.moodcode.global.auth.jwt.TokenProvider;
import com.devcrew.moodcode.global.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Value("${jwt.refresh-expiry-seconds}")
    private long refreshExpirySeconds;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // CustomOAuth2UserService에서 저장한 정보를 기반으로 Email 추출
        // (구글/카카오에 따라 속성 키가 다르므로 OAuthAttributes 로직을 재사용하거나, Attributes에서 email 찾기)
        // 여기서는 OAuthAttributes가 User를 저장할 때 email을 사용했다고 가정
        String email = null;
        if(oAuth2User.getAttributes().containsKey("email")) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else {
            // 카카오의 경우 구조가 달라서 별도 추출 로직 필요 (간소화를 위해 생략, 실제론 Map 탐색 필요)
            // 예: ((Map)oAuth2User.getAttributes().get("kakao_account")).get("email")
            log.info("이메일 추출 로직 확인 필요: {}", oAuth2User.getAttributes());
        }

        // 이메일로 유저 찾기 (반드시 존재함)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        // 토큰 발급
        String accessToken = tokenProvider.createToken(user.getId(), user.getRole());
        String refreshToken = tokenProvider.createRefreshToken();

        // Redis 저장
        redisService.setValues("RT:" + user.getId(), refreshToken, Duration.ofSeconds(refreshExpirySeconds));

        // 프론트엔드로 리다이렉트 (토큰을 쿼리 파라미터로 전달)
//        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/callback")
//                .queryParam("accessToken", accessToken)
//                .queryParam("refreshToken", refreshToken)
//                .build().toUriString();

        // [임시 - 테스트용] 프론트 없을 때 8080으로 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}