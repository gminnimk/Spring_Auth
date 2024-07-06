package com.sparta.springauth.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/*
✅ Spring Boot 애플리케이션에서 Spring Security를 설정하는 구성 클래스.

    ➡️ 이 클래스는 보안 설정을 정의하여 애플리케이션의 HTTP 요청을 관리.

    ➡️ 특정 경로에 대한 접근을 허용하거나 인증을 요구하고, 사용자 인증을 처리하는 방식을 지정.



    📢 Spring Security : 인증, 권한 관리 그리고 데이터 보호 기능을 포함하여 웹 개발 과정에서
                         필수적인 사용자 관리 기능을 구현하는데 도움을 주는 Spring의 강력한 프레임워크
 */


@Configuration // 이 클래스가 Spring 설정 클래스임을 표시.
@EnableWebSecurity // Spring Security 지원을 가능하게 함,  Spring Security를 활성화하여 보안 기능을 사용할 수 있도록 함.
public class WebSecurityConfig {

    @Bean // 이 메서드가 Spring 컨텍스트에서 관리하는 빈을 생성함을 나타냄.

    // 보안 필터 체인을 정의하는 메서드 , HttpSecurity를 인자로 받아 보안 설정을 구성
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());
        /*
        CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화.
        일반적으로 API 서버에서는 CSRF 보호가 필요하지 않기 때문에 이를 비활성화(disable)할 수 있음.

        📢 CSRF : 사이트 간 요청 위조, Cross-site request forgery

                - 공격자가 인증된 브라우저에 저장된 쿠키의 세션 정보를 활용하여 웹 서버에 사용자가 의도하지 않은 요청을 전달하는 것.
         */


        // HTTP 요청에 대한 인가 설정을 구성
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                // HTTP 요청에 대한 인가 설정을 구성합니다
                authorizeHttpRequests
                        // resources 접근 허용 설정 ,정적 리소스(예: CSS, JavaScript, 이미지)에 대한 요청은 인증 없이 접근을 허용.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // '/api/user/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers("/api/user/**").permitAll()
                        // 그 외 모든 요청 인증처리
                        .anyRequest().authenticated()
        );


        // 로그인 사용
        http.formLogin((formLogin) ->
                formLogin
                        // 로그인 View 제공 (GET /api/user/login-page)
                        .loginPage("/api/user/login-page")
                        // 로그인 처리 (POST /api/user/login)
                        .loginProcessingUrl("/api/user/login")
                        // 로그인 처리 후 성공 시 URL
                        .defaultSuccessUrl("/")
                        // 로그인 처리 후 실패 시 URL
                        .failureUrl("/api/user/login-page?error")
                        .permitAll()
        );

        // 구성된 보안 필터 체인을 반환. 이 필터 체인이 애플리케이션의 보안 설정을 담당.
        return http.build();
    }
}





