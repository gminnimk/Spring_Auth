package com.sparta.springauth.dto;

import lombok.Getter;
import lombok.Setter;

/*
✅ 사용자 로그인 요청에 필요한 데이터를 담는 DTO(Data Transfer Object).

    ➡️ DTO는 계층간 데이터 교환을 위한 객체, 이 클래스를 통해 클라이언트에서 서버로 사용자 로그인에 필요한 데이터를 전송할 수 있음.
 */

@Setter
@Getter
public class LoginRequestDto {
    // username , password는 사용자 로그인에 필요한 정보를 나타내는 필드.
    private String username;
    private String password;
}