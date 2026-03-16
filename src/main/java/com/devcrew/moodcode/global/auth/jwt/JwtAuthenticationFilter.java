package com.devcrew.moodcode.global.auth.jwt;

import com.devcrew.moodcode.global.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RedisService redisService; // 블랙리스트 확인용

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        // 1. 토큰이 있고, 유효성 검사를 통과했는지?
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

            // 2. [추가된 보안] Redis에 블랙리스트로 등록된 토큰인지 확인
            // 로그아웃 하면 Redis에 "logout" 키로 등록됨 -> 접근 거부
            if (redisService.hasKey(token)) {
                log.warn("블랙리스트에 등록된 토큰입니다. 접근 거부");
                // 401 에러를 명시적으로 주고 싶다면 여기서 response.sendError() 사용
                // 여기서는 단순히 컨텍스트 설정을 안 해서 뒤쪽 필터에서 걸러지게 함
            } else {
                // 3. 정상 토큰이면 SecurityContext에 저장
                Long userId = tokenProvider.getUserId(token);
                String role = tokenProvider.getRole(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // Principal 자리에 userId(Long) 저장
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}