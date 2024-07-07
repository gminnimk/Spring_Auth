package com.sparta.springauth.config;

import com.sparta.springauth.jwt.JwtAuthorizationFilter;
import com.sparta.springauth.jwt.JwtAuthenticationFilter;
import com.sparta.springauth.jwt.JwtUtil;
import com.sparta.springauth.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
✅ Spring Boot 애플리케이션에서 Spring Security를 설정하는 구성 클래스.

    ➡️ 이 클래스는 보안 설정을 정의하여 애플리케이션의 HTTP 요청을 관리.

    ➡️ 특정 경로에 대한 접근을 허용하거나 인증을 요구하고, 사용자 인증을 처리하는 방식을 지정.



    📢 Spring Security : 인증, 권한 관리 그리고 데이터 보호 기능을 포함하여 웹 개발 과정에서
                         필수적인 사용자 관리 기능을 구현하는데 도움을 주는 Spring의 강력한 프레임워크
 */


@Configuration // 이 클래스가 Spring 설정 클래스임을 표시
@EnableWebSecurity // Spring Security를 활성화하여 보안 기능을 사용할 수 있도록 함
public class WebSecurityConfig {

    private final JwtUtil jwtUtil; // JWT 토큰 관련
    private final UserDetailsServiceImpl userDetailsService; // 사용자 정보를 DB에서 가져오는 데 사용
    private final AuthenticationConfiguration authenticationConfiguration; // 인증 관련 설정을 담당

    // 생성자를 통해 JwtUtil, UserDetailsServiceImpl, AuthenticationConfiguration을 주입받음
    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean // 이 메서드가 Spring 컨텍스트에서 관리하는 빈을 생성함을 나타냄
    // AuthenticationManager 는 인증 요청을 처리하는 인터페이스.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // AuthenticationManager를 빈으로 등록하여 인증 관리를 담당
        return configuration.getAuthenticationManager();
    }

    @Bean // 이 메서드가 Spring 컨텍스트에서 관리하는 빈을 생성함을 나타냄
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        // JwtAuthenticationFilter를 빈으로 등록
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean // 이 메서드가 Spring 컨텍스트에서 관리하는 빈을 생성함을 나타냄
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        // JwtAuthorizationFilter를 빈으로 등록
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean // 이 메서드가 Spring 컨텍스트에서 관리하는 빈을 생성함을 나타냄
    // SecurityFilterChain은 보안 필터 체인을 나타내며, HttpSecurity 객체를 인자로 받아 생성
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());
        /*
        CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화.
        일반적으로 API 서버에서는 CSRF 보호가 필요하지 않기 때문에 이를 비활성화(disable)할 수 있음.

        📢 CSRF : 사이트 간 요청 위조, Cross-site request forgery

                - 공격자가 인증된 브라우저에 저장된 쿠키의 세션 정보를 활용하여 웹 서버에 사용자가 의도하지 않은 요청을 전달하는 것.
         */

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        /*
        세션 관리를 Stateless로 설정하여 서버가 세션을 생성하거나 유지하지 않도록 함.
        JWT를 사용하기 때문에 상태 없는(stateless) 인증 방식을 채택함.
        */

        // HTTP 요청에 대한 인가 설정을 구성
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        // resources 접근 허용 설정, 정적 리소스(예: CSS, JavaScript, 이미지)에 대한 요청은 인증 없이 접근을 허용.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // '/api/user/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers("/api/user/**").permitAll()
                        // 그 외 모든 요청 인증처리
                        .anyRequest().authenticated()
        );

        // 로그인 설정
        http.formLogin((formLogin) ->
                        formLogin
                                // 로그인 View 제공 (GET /api/user/login-page)
                                .loginPage("/api/user/login-page").permitAll()
                // 로그인 페이지에 대한 접근을 인증 없이 허용
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        /*
        JwtAuthorizationFilter와 JwtAuthenticationFilter를 필터 체인에 추가.
        jwtAuthorizationFilter()는 jwtAuthenticationFilter()보다 먼저 실행됨.
        */

        // 구성된 보안 필터 체인을 반환. 이 필터 체인이 애플리케이션의 보안 설정을 담당.
        return http.build();
    }
}


