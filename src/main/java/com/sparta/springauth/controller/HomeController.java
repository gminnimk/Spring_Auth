package com.sparta.springauth.controller;

import com.sparta.springauth.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
✅ HomeController 라는 이름을 가진 스프링 MVC 컨트롤러.
 */

@Controller // 이 클래스를 스프링 MVC의 컨트롤러로 선언.
public class HomeController {

    /*
    HTTP GET 요청이 “/” URL로 들어오면 home 메소드를 실행하도록 설정되어 있습니다.
    home 메소드는 Model 객체에 "username"이라는 이름으로 "username"이라는 값을 추가하고,
    "index"라는 이름의 뷰를 반환합니다. 이 "index"는 실제로 표시할 뷰를 찾는 데 사용됩니다.
     */

    /*
    📢 현 문제 상황 : 로그인 후 실제 사용자 이름 대신 "username"이라는 고정된 값이 표시되는 것.

        주요 변경 사항:

                     (1). @AuthenticationPrincipal UserDetailsImpl userDetails 파라미터 추가

                            - @AuthenticationPrincipal 어노테이션은 현재 인증된 사용자의 정보를 주입받기 위해 사용
                            - UserDetailsImpl은 사용자 정보를 담고 있는 클래스로, Spring Security의 UserDetails 인터페이스를 구현한 클래스


                     (2). if (userDetails != null) 조건문 추가 => 사용자가 인증(로그인)되었는지 확인하는 역할

                     (3). 동적인 사용자 이름 설정 :

                            - model.addAttribute("username", userDetails.getUsername());
                            - 기존에는 "username"이라는 고정된 문자열을 사용, 이제는 실제 로그인한 사용자의 이름을 get
     */

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "index";
    }
}