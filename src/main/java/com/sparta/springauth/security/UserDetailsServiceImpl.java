package com.sparta.springauth.security;

import com.sparta.springauth.entity.User;
import com.sparta.springauth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 ✅ Spring Security에서 사용자 인증을 처리하기 위해 UserDetailsService 인터페이스를 구현한 UserDetailsServiceImpl 클래스.

    ➡️ 클래스는 주어진 사용자 이름(사용자 아이디)을 기반으로 DB에서 사용자를 조회하고,
       조회된 사용자 정보를 UserDetails 인터페이스를 구현한 UserDetailsImpl 객체로 변환하여 반환.

    📢 UserDetailsService : username/password 인증방식을 사용할 때 사용자를 조회하고 검증한 후 UserDetails를 반환,
                            Custom하여 Bean으로 등록 후 사용 가능.

 */

@Service // Spring에 의해 빈으로 관리되는 서비스 클래스
public class UserDetailsServiceImpl implements UserDetailsService { // 인터페이스를 구현하는 UserDetailsServiceImpl 클래스

    private final UserRepository userRepository; // 사용자 정보를 조회할 때 사용할 UserRepository 인스턴스

    public UserDetailsServiceImpl(UserRepository userRepository) { // UserRepository를 주입받아 필드에 할당
        this.userRepository = userRepository;
    }

    @Override
    // 주어진 사용자 이름(사용자 아이디)을 기반으로 사용자 정보를 로드하는 메서드입니다.
    // 이 메서드는 UserDetailsService 인터페이스에서 정의된 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username) // UserRepository를 사용하여 데이터베이스에서 주어진 사용자 이름으로 사용자 정보를 조회합니다.
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username)); // 사용자가 존재하지 않으면 UsernameNotFoundException을 발생시킵니다.

        return new UserDetailsImpl(user);
        // 조회된 사용자 정보를 UserDetailsImpl 객체로 변환하여 반환합니다. UserDetailsImpl은 UserDetails 인터페이스를 구현한 클래스로,
        // Spring Security가 사용자 인증 및 인가를 처리하는 데 필요한 사용자 정보를 제공합니다.
    }
}