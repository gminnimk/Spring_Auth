package com.sparta.springauth.security;

import com.sparta.springauth.entity.User;
import com.sparta.springauth.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority; //  Spring Security에서 사용자의 권한을 나타내는 인터페이스와 클래스
import org.springframework.security.core.authority.SimpleGrantedAuthority; //  Spring Security에서 사용자의 권한을 나타내는 인터페이스와 클래스
import org.springframework.security.core.userdetails.UserDetails; // Spring Security에서 사용자 정보를 나타내는 인터페이스

import java.util.ArrayList;
import java.util.Collection;

/*
 ✅ Spring Security에서 사용자 인증 및 인가를 처리하기 위해 사용하는 UserDetails 인터페이스의 구현 클래스.

    ➡️ 이 클래스는 사용자 정보를 캡슐화하여 Spring Security가 사용자를 인증하고 인가할 수 있도록 함.

    📢 UserDetails : 검증된 UserDetails는 UsernamePasswordAAuthenticationToken 타입의 Authentication을 만들 때 사용되며
       해당 인증객체는 SecurityContextHolder에 세팅됨, Custom하여 사용가능.q

    📢 UsernamePasswordAAuthenticationToken : Authentication을 implements한 AbstractAuthentication Token의 하위 클래스로,
                                              인증객체를 만드는데 사용.
 */

// 이 클래스가 UserDetails 인터페이스를 구현한다고 선언.
public class UserDetailsImpl implements UserDetails {

    private final User user; // User 객체를 저장하는 필드

    public UserDetailsImpl(User user) { // User 객체를 받아서 필드에 저장
        this.user = user;
    }

    public User getUser() { // 저장된 User 객체를 반환하는 메서드
        return user;
    }

    @Override
    public String getPassword() { //사용자 비밀번호를 반환하는 메서드
        return user.getPassword();
    }

    @Override
    public String getUsername() { // 사용자 이름(아이디)를 반환하는 메서드
        return user.getUsername();
    }

    @Override
    // getAuthorities 라는 메서드를 선언.
    // 이 메서드는 Collection<? extends GrantedAuthority> 타입을 반환,
    // GrantedAuthority는 Spring Security에서 권한을 나타내는 인터페이스, 이 메서드는 사용자의 모든 권한을 컬렉션으로 반환.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole(); // user 객체의 getRole 메서드를 호출해 사용자 역할을 가져옵니다.
        String authority = role.getAuthority(); // 역할에 해당하는 권한 문자열을 가져옵니다.


        // 권한 문자열을 SimpleGrantedAuthority 객체로 감쌈.
        // SimpleGrantedAuthority는 GrantedAuthority 인터페이스를 구현한 클래스로, 권한을 나타냄.
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);


        // 권한을 저장할 컬렉션을 생성
        // ArrayList는 Collection 인터페이스를 구현한 클래스로, 동적 배열을 나타냄.
        Collection<GrantedAuthority> authorities = new ArrayList<>();


        // 컬렉션에 권한을 추가
        authorities.add(simpleGrantedAuthority);

        return authorities; // 권한 컬렉션을 반환
    }

    @Override

    // 계정이 만료되지 않았는지 여부를 반환.
    // 여기서는 항상 true를 반환하여 계정이 만료되지 않았음을 나타냅니다.
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override

    // 계정이 잠기지 않았는지 여부를 반환.
    // 여기서는 항상 true를 반환하여 계정이 잠기지 않았음을 나타냅니다.
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 자격 증명이 만료되지 않았는지 여부를 반환.
    // 여기서는 항상 true를 반환하여 자격 증명이 만료되지 않았음을 나타냅니다.
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 계정이 활성화되었는지 여부를 반환.
    // 여기서는 항상 true를 반환하여 계정이 활성화되었음을 나타냅니다.
    public boolean isEnabled() {
        return true;
    }
}