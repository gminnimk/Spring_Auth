package com.sparta.springauth.filter;

import com.sparta.springauth.entity.User;
import com.sparta.springauth.jwt.JwtUtil;
import com.sparta.springauth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/*
✅ Spring Securiy에서 인증 필터로 사용되며, HTTP 요청이 들어올 때마다 특정 작업을 수행.

     ➡️주로 JWT 토큰을 검증, 토큰에 포함된 사용자 정보를 DB에 조회하여 요청에 추가하는 역할.
 */


// 로깅을 위한 설정으로, AuthFilter라는 이름의 로거를 생성.
@Slf4j(topic = "AuthFilter")

// Spring에서 이 클래스를 빈으로 등록.
// @Component // SpringSecurity 를 사용하기 위해 주석처리.

// 이 필터의 실행 순서를 지정. 숫자가 낮을수록 먼저 실행.
@Order(2)

// 이 클래스가 Filter 인터페이스를 구현한다고 선언.
public class AuthFilter implements Filter {

    private final UserRepository userRepository; // UserRepository는 사용자 정보를 DB에 가져오는 데 사용
    private final JwtUtil jwtUtil; // JwtUtil은 JWT 토큰 관련 유틸리티 클래스.

    public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }




    // doFilter 메서드는 필터의 핵심 기능을 정의. 요청이 필터를 통과할 때마다 이 메서드가 호출.
    // HttpServletRequest로 요청을 캐스팅하고, 요청된 URL을 가져옴.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(url) &&
                (url.startsWith("/api/user") || url.startsWith("/css") || url.startsWith("/js"))
        ) {
            // /api/user, /css, /js로 시작하는 URL은 인증 없이 요청을 진행.
            chain.doFilter(request, response); // 다음 Filter 로 이동
        } else {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest); // jwtUtil.getTokenFromRequest(httpServletRequest): 요청에서 토큰을 추출.

            if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                // JWT 토큰 substring
                String token = jwtUtil.substringToken(tokenValue); // 토큰이 존재하면, jwtUtil.substringToken(tokenValue): 토큰에서 Bearer 접두사를 제거.

                // 토큰 검증
                if (!jwtUtil.validateToken(token)) { // 토큰을 검증, 유효하지 않으면 예외를 던짐.
                    throw new IllegalArgumentException("Token Error");
                }

                // 토큰에서 사용자 정보 가져오기
                Claims info = jwtUtil.getUserInfoFromToken(token); // jwtUtil.getUserInfoFromToken(token): 토큰에서 사용자 정보를 가져옴.

                User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() ->
                        // userRepository.findByUsername(info.getSubject()): 사용자 정보를 DB에서 조회, 사용자가 없으면 예외를 던짐.
                        new NullPointerException("Not Found User")
                );

                request.setAttribute("user", user);
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                throw new IllegalArgumentException("Not Found Token");
            }
        }
    }

}