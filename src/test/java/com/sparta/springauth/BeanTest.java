package com.sparta.springauth;


import com.sparta.springauth.food.Food;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/*
✅ Bean과 Qualifier 어노테이션을 테스트하는 클래스

    ➡️ Food 인터페이스 타입의 Bean 객체를 주입받아 eat() 메서드를 호출하는 것을 테스트함.

    ➡️ @Qualifier 어노테이션을 사용하여 특정 이름의 Bean을 주입받는 것을 확인.
 */

@SpringBootTest // 스프링 부트 테스트 환경을 설정, 이 어노테이션을 사용하면 스프링이 관리하는 Bean을 주입받을 수 있음.

public class BeanTest {

    @Autowired // Food 타입의 Bean 객체를 주입받고, 같은 타입의 Bean이 여러 개 있을 경우 Bean 이름으로 찾는다
    @Qualifier("pizza") // 같은 타입의 Bean이 여러개 있을 때, 'pizza'라는 이름의 Bean을 주입받음.
    Food food;

    @Test // JUnit 테스트 메서드임을 선언.
    @DisplayName("Primary 와 Qualfier 우선순위 확인")
    void test1() {
        food.eat(); // 주입받은 Food 타입의 Bean 객체의 eat 메서드를 호출.
    }
}
