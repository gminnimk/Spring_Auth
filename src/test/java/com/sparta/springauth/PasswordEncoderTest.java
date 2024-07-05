package com.sparta.springauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
✅ 이 클래스는 PasswordEncoder를 사용하여 비밀번호를 암호화하고, 암호화된 비밀번호와 입력된 비빌먼호가 일치하는지 확인하는 테스트를 수행.
 */

@SpringBootTest // 스프링 부트 테스트 환경을 설정하여 애플리케이션 컨텍스트를 로드
public class PasswordEncoderTest {

    @Autowired // 스프링 컨텍스트에서 PasswordEncoder 빈(Bean)을 주입 받음
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("수동 등록한 passwordEncoder를 주입 받아와 문자열 암호화") // 테스트의 의도를 설명하는 어노테이션
    void test1() { // 해당 메서드는 비밀번호 암호화와 비교를 테스트
        String password = "Robbie's password"; // 암호화할 원본 비밀번호 문자열

        // 암호화
        String encodePassword = passwordEncoder.encode(password); // 주어진 문자열 비밀번호를 암호화
        System.out.println("encodePassword = " + encodePassword); // 암호화된 비밀번호 출력

        String inputPassword = "Robbie"; // 비교할 입력 비밀번호

        // 복호화를 통해 암호화된 비밀번호와 비교
        boolean matches = passwordEncoder.matches(inputPassword, encodePassword); // 입력 비밀번호와 암호화된 비밀번호 비교
        System.out.println("matches = " + matches); // 비교 결과 출력 (암호화할 때 사용된 값과 다른 문자열과 비교했기 때문에 false)
    }
}
