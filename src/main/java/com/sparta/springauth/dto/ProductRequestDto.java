package com.sparta.springauth.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

/*
✅ 이 클래스는 제품 정보를 전달하기 위한 데이터 전송 객체(DTO)입니다.

    ➡️ 클라이언트로부터 받은 제품 정보의 유호성을 검사하는 데 사용.

    📢 유효성 검사 : 각 필드에 적용된 어노테이션을 통해 입력된 데이터의 유효성을 검사.

            @NotBlank: 제품 이름이 비어있지 않아야 함을 보장합니다.

            @Email: 입력된 이메일 주소가 올바른 형식인지 확인합니다.

            @Positive와 @Negative: 가격은 양수, 할인은 음수여야 함을 강제합니다.

            @Size: 제품 링크의 길이를 제한합니다.

            ㄴ@Max와 @Min: 최대 및 최소 수량에 제한을 둡니다.
 */


@Getter
public class ProductRequestDto {
    @NotBlank // 문자열이 null이 아니고, 공백이 아닌 문자를 하나 이상 포함해야 함
    private String name;

    @Email // 유효한 이메일 형식이어야 함
    private String email;

    @Positive(message = "양수만 가능합니다.") // 0보다 큰 양수여야 함
    private int price;

    @Negative(message = "음수만 가능합니다.") // 0보다 작은 음수여야 함
    private int discount;

    @Size(min=2, max=10) // 문자열의 길이가 2에서 10 사이여야 함
    private String link;

    @Max(10) // 최대값이 10이어야 함
    private int max;

    @Min(2) // 최소값이 2여야 함
    private int min;
}


