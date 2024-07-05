package com.sparta.springauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
✅ PasswordConfig 클래스는 스프링 시큐리티 설정 클래스.

    ➡️ 이 클래스는 비밀번호를 저장하기 위해 암호화하는 데 사용되는 PasswordEncoder 빈(Bean)을 정의.
 */


// @Configuration 어노테이션은 이 클래스가 스프링 설정 클래스임을 나타냅니다.
@Configuration
public class PasswordConfig {

    // @Bean 어노테이션은 이 메서드가 스프링 컨테이너에 의해 관리되는 빈(Bean) 객체임을 나타냅니다.
    // BCryptPasswordEncoder 객체를 스프링 컨테이너에 빈으로 등록 ➡️ 애플리케이션의 다른 부분에서 PasswordEncoder를 주입받아 사용할 수 있음.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder는 비밀번호를 암호화하는 데 사용되는 클래스입니다.
        // ➡️ DB에 저장된 비밀번호가 노출되더라도 실제 비밀번호가 유출도지 않음.
        return new BCryptPasswordEncoder();
    }
}
