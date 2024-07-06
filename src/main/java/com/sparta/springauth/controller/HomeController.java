package com.sparta.springauth.controller;

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
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("username", "username");
        return "index";
    }
}