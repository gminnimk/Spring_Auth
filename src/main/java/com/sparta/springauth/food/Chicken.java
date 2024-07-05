package com.sparta.springauth.food;

import org.springframework.stereotype.Component;

@Component // @Component 에노테이션을 명시함으로 써 Bean으로 등록.

/*
    ➡️ implements 키워드는 자바에서 특정 인터페이스를 구현하겠다는 것을 나타냄.

    📢
    Chicken 클래스는 Food 인터페이스를 구현하고 있으므로, Food 인터페이스에 정의된 모든 메서드를 Chicken 클래스에서 구현해야 함.
    아래 클래스의 해당 implements 키워드는 Chicken 클래스가 Food 인터페이스의 계약을 준수하겠다는 것을 지칭!
    이를 통해 다양한 음식 클래스가 Food 인터페이스를 구현하면, 각각의 eat() 메서드를 통해 다양한 음식을 먹는 행동을 표현 가능.
    위 설명이 바로 인터페이스와 implements 키워드의 역할.
 */

public class Chicken implements Food {
    @Override
    public void eat() {
        System.out.println("치킨을 먹습니다.");
    }
}