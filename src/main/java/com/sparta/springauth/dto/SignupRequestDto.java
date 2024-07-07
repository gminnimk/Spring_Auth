package com.sparta.springauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/*
✅ 사용자 등록 요청에 필요한 데이터를 담는 DTO(Data Transfer Object).

    ➡️ DTO는 계층간 데이터 교환을 위한 객체로, 일반적으로 웹 요청과 응답에서 사용.

    ➡️ 이 클래스를 통해 클라이언트에서 서버로 사용자 등록에 필요한 데이터를 안전하게 전송할 수 있음.
 */

@Getter
@Setter
public class SignupRequestDto {

    // 사용자 등록에 필요한 정보를 나타내는 필드.
    @NotBlank
    private String username;
    @NotBlank
    private String password;
//    @Email
    // 3주차 숙제 : @Pattern 사용해서 @Email 대체
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank
    private String email;

    // admin 필드는 사용자가 관리자인지 아닌지를 나타냄, 기본값은 false(관리자 X)
    private boolean admin = false;

    // 관리자 등록을 위한 토큰, 기본값은 빈 문자열
    private String adminToken = "";
}


