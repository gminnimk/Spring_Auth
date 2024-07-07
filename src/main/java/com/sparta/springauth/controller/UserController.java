package com.sparta.springauth.controller;

import com.sparta.springauth.dto.LoginRequestDto;
import com.sparta.springauth.dto.SignupRequestDto;
import com.sparta.springauth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/*
✅ 스프링 MVC 컨트롤러, 사용자 관련 요청을 처리하는 역할.
 */
// Lombok 라이브러리에서 제공하는 어노테이션으로, 클래스에 로깅 기능을 쉽게 추가할 수 있게 함.
// 별도로 로깅 인스턴스를 생성하지 않고도 로그를 기록할 수 있음.
@Slf4j
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

    // @Valid SignupRequestDto requestDto: 클라이언트로부터 받은 회원가입 데이터를 SignupRequestDto 객체로 변환하고,
    // @Valid 어노테이션을 통해 유효성 검사를 수행합니다.
    // BindingResult bindingResult: 유효성 검사 결과를 담는 객체입니다.
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {

        // Validation 예외처리
        // bindingResult.getFieldErrors()로 유효성 검사 중 발생한 모든 필드 에러를 가져옵니다.
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            // 각 필드 에러에 대해 로그를 출력, 에러가 발생한 필드명과 에러 메시지를 기록합니다.
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }


        // UserService의 signup 메소드를 호출하여 사용자 등록을 처리합니다.
        userService.signup(requestDto);

        // 사용자 등록이 완료되면 로그인 페이지로 리다이렉트합니다.
        return "redirect:/api/user/login-page";
    }


    // Spring Security를 이용하여 Filter에서 처리하기 때문에 주석처리.
//    // @PostMapping 어노테이션을 사용하여 HTTP POST 요청이 "/api/user/login" URL로 들어올 때 이 메소드를 실행하도록 함.
//    // LoginRequestDto 객체로 변환되어 이 메소드에 전달이 되고 userService.login(request, res); 코드를 통해 로그인 처리.
//    @PostMapping("/user/login")
//    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
//        try {
//            // UserService의 login 메소드를 호출하여 로그인을 처리, 이 메소드는 사용자의 로그인 요청 정보를 인자로 받아 처리하며,
//            // 로그인 성공 시 JWT 토큰을 생성하고 이를 응답 헤더에 추가.
//            userService.login(requestDto, res);
//        } catch (Exception e) {
//            // 로그인 처리 중 예외가 발생하면, 에러 쿼리 파라미터와 함께 로그인 페이지로 리다이렉트.
//            return "redirect:/api/user/login-page?error";
//        }
//        return "redirect:/"; // 로그인이 성공적으로 처리되면, 메인 페이지("/")로 리다이렉트.
//    }
}