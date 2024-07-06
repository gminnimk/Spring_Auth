package com.sparta.springauth.entity;

/*
✅ 사용자의 권한을 나타내는 UserRoleEnum 열거형 클래스.

    ➡️ 이 클래스를 통해 사용자가 일반 사용자인지, 관리자인지 판단여부 가능,

    ➡️ 이 클래스는 사용자의 권한을 관리하고, 각 권한에 대한 정보를 제공.
 */



/*
UserRoleEnum이라는 이름의 열거형(enum)을 선언하는 부분입니다.
USER와 ADMIN이라는 두 가지 권한을 정의하고 있습니다.
각 권한은 Authority 클래스의 상수를 인자로 받습니다.
 */
public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    // authority는 각 권한의 실제 값을 저장하는 필드, 이 필드는 final로 선언되어 있어 한 번 할당된 후에는 변경 X
    private final String authority;

    // 이 부분은 UserRoleEnum의 생성자, 각 권한의 실제 값을 인자로 받아 authority 필드에 저장.
    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    // getAuthority 메서드는 authority 필드의 값을 반환하는 getter 메서드.
    public String getAuthority() {
        return this.authority;
    }

    // Authority 는 UserRoleEnum 내부에 정의된 정적 내부 클래스, 이 클래스는 사용자 권한의 실제 값을 상수로 정의.
    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}