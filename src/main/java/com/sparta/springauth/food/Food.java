package com.sparta.springauth.food;


// 인터페이스 옆 콩 모양을 클릭하면 Choose Bean이 나오는데
// Food 타입으로 2개의 구현체를 만든 후 Bean으로 등록한 것.




/*
✅ interface는 클래스나 프로그램이 가져야 하는 메서드와 필드를 정의하는 역할.

    ➡️ Food 인터페이스에서는 eat() 라는 메서드를 정의하고 있고, 이 eat() 메서드는 Food 인터페이스를 구현하는 모든 클래스(ex.chicken, pizza)에서 반드시 구현해야 함.

    📢 interface의 주요 역할

    (1). 다형성 : interface를 구현하는 여러 클래스는 같은 interface 타입으로 참조될 수 있습니다. 이를 통해 다양한 구현체를 동일한 방식으로 처리할 수 있습니다.

    (2). 계약 : interface는 클래스가 가져야 하는 메서드를 명시함으로써 클래스의 기본적인 행동을 정의합니다. 이를 통해 클래스의 사용 방법을 명확하게 알 수 있습니다.

    따라서 Food 인터페이스는 eat() 메서드를 가진 모든 클래스가 Food 타입으로 췩브될 수 있도록 하며, 이들 클래스가 어떤 행동(eat())을 해야 하는지를 명시해야함!

 */

public interface Food {
    void eat(); // 각각 Chicken과 Pizza 클래스에서 해당 메서드를 구현.
}