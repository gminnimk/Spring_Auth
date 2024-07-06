package com.sparta.springauth.controller;

import com.sparta.springauth.dto.SignupRequestDto;
import com.sparta.springauth.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/*
✅ 스프링 MVC 컨트롤러, 사용자 관련 요청을 처리하는 역할.
 */


@Controller
@RequestMapping("/api") // @RequestMapping 어노테이션을 사용하여 이 컨트롤러의 모든 매핑이 "/api"로 시작하도록.
public class UserController {

    // UserService 객체를 private final로 선언하여, 한 번 할당되면 변경할 수 없도록 함.
    private final UserService userService;

    // UserContrrller의 생성자로, UserService 객체를 주입받아 초기화.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping 어노테이션을 사용하여 HTTP GET 요청이 "/api/user/login-page" URL로 들어올 때 이 메소드를 실행.
    @GetMapping("/user/login-page")
    public String loginPage() {
        // // "login"이라는 이름의 뷰를 반환합니다. 스프링은 이 이름을 사용하여 실제로 표시할 뷰를 찾습니다
        return "login";
    }


    // @GetMapping 어노테이션을 사용하여 HTTP GET 요청이 "/api/user/signup" URL로 들어올 때 이 메소드를 실행하도록 합니다.
    @GetMapping("/user/signup")
    public String signupPage() {
        // "signup"이라는 이름의 뷰를 반환합니다. 스프링은 이 이름을 사용하여 실제로 표시할 뷰를 찾습니다.
        return "signup";
    }

    // @PostMapping 어노테이션을 사용하여 HTTP POST 요청이 "/api/user/signup" URL로 들어올 때 이 메소드를 실행하도록 합니다.
    // signup 메소드는 실제 회원가입을 처리하는 역할, SignyupRequestDto 객체를 인자로 받아 UserService의 signyp 메소드를 호출하여 회원가입을 처리
    @PostMapping("/user/signup")
    public String signup(SignupRequestDto requestDto) {
        // UserService의 signup 메소드를 호출하여 사용자 등록을 처리합니다.
        userService.signup(requestDto);

        // 사용자 등록이 완료되면 로그인 페이지로 리다이렉트합니다.
        return "redirect:/api/user/login-page";
    }
}