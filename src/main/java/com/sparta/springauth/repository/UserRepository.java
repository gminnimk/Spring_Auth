package com.sparta.springauth.repository;

import com.sparta.springauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
✅ JPA 리포지토리 인터페이스, DB와의 상호작용을 담당하는 인터페이스로, DB에서 데이터를 조회하거나 조작할 수 있음.

    ➡️ 해당 인터페이스는 User 엔티티와 관련된 DB 작업을 처리.
 */


// 해당 인터페이스는 JpaRepository를 상속받으며, User 엔티티와 id 의 타입인 Lomg을 지정.
public interface UserRepository extends JpaRepository<User, Long> {

    // 이 메소드는 username을 매개변수로 받아 User 엔티티를 조회.
    Optional<User> findByUsername(String username);

    // 이 메소드는 email을 매개변수로 받아 User 엔티티를 조회, 반환 타입은 Optional<User>
    Optional<User> findByEmail(String email);


}


