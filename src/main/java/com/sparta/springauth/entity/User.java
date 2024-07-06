package com.sparta.springauth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
✅ JPA 엔티티 클래스, DB의 테이블가 매핑되는 클래스로, 이 클래스의 인스턴스는 테이블의 레코드에 해당.
 */


@Entity // 이 클래스를 JPA 엔티티로 선언
@Getter
@Setter
@NoArgsConstructor // 이 클래스의 기본 생성자를 자동으로 추가
@Table(name = "users") // 이 엔티티가 매핑될 테이블의 이름을 "users"로 지정
public class User {
    @Id // 이 필드를 테이블의 기본 키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값 생성 전략을 IDENTITY로 지정
    private Long id;

    // @Column 어노테이션을 사용하여 이 필드를 테이블의 컬럼으로 지정하고,
    // nullable = false, unique = true 옵션을 설정하여 이 컬럼이 null 값을 허용하지 않고, 유일한 값을 가져야 함을 지정
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    // @Enumerated 어노테이션을 사용하여 이 필드가 열거형임을 지정하고,
    // EnumType.STRING로 설정하여 열거형의 이름을 문자열로 데이터베이스에 저장
    @Enumerated(value = EnumType.STRING) // USER -> USER 로 저장됨
    private UserRoleEnum role;

    // 사용자 정의 생성자입니다. username, password, email, role 값을 인자로 받아 User 객체를 생성
    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}