package com.sparta.springauth.controller;

import com.sparta.springauth.entity.User;
import com.sparta.springauth.entity.UserRoleEnum;
import com.sparta.springauth.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/*
✅ Spring MVC의 컨트롤러, HTTP 요청을 처리하고 응답을 생성하는 역할.

    ➡️ JWT 인증이 제대로 이루어지는지 확인하는 데 사용.
 */



@Controller // 이 클래스가 Spring MVC의 컨트롤러임을 나타냄.
@RequestMapping("/api") // 이 클래스의 모든 요청 경로 앞에 /api가 추가.
public class ProductController {

    @GetMapping("/products") // HTTP GET 요청을 처리, /api/products 경로에 매핑.
    public String getProducts(HttpServletRequest req) { // HttpServletRequest 객체를 인자로 받아 요청 정보를 사용.
        System.out.println("ProductController.getProducts : 인증 완료"); // 요청이 이 메서드로 들어왔는지 확인.
        User user = (User) req.getAttribute("user"); //  user 객체를 가져옴. 이 user 객체는 인증 필터에서 설정된 사용자 정보. 만약 인증이 제대로 되었다면, user 객체는 요청에 설정되어 있을 것
        System.out.println("user.getUsername() = " + user.getUsername()); // 가져온 user 객체의 사용자 이름을 콘솔에 출력. 이를 통해 인증이 성공, 해당 사용자의 정보를 확인

        return "redirect:/"; // 요청을 처리한 후 / 경로로 사용자를 이동.
    }


    // 관리자용
    //  @Secured("권한 이름") : 이 어노테이션은 해당 메서드에 접근할 수 있는 사용자 역할을 제한. (권한 설정)
    // UserRoleEnum.Authority.ADMIN 은 관리자 권한을 의미, 관리자 권한을 가진 사용자만 이 메서드에 접근할 수 있음
    @Secured(UserRoleEnum.Authority.ADMIN)

    // HTTP GET 요청을 처리하며, /api/products/secured 경로에 매핑.
    // 해당 경로로 들어오는 GET 요청은 getProductsByAdmin 메서드가 처리.
    @GetMapping("/products/secured")

    // @AuthenticationPrincipal UserDetailsImpl userDetails: 이 어노테이션은 현지 인증된 사용자 정보를 주입받음.
    // UserDetailsImpl 은 Spring Security에서 제공하는 UserDetails 인터페이스의 구현체로, 인증된 사용자의 세부 정보를 담고 있음.
    public String getProductsByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());

        // 현재 인증된 사용자가 가진 권한(역할)을 반복.
        // GrantedAuthority 는 사용자가 가진 권한을 나타내는 인터페이스.
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            System.out.println("authority.getAuthority() = " + authority.getAuthority());
        }

        return "redirect:/";
    }
}