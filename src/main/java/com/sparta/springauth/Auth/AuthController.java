package com.sparta.springauth.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
✅ 인증 관련 작업을 처리하는 클래스, 쿠키와 세션을 이용하여 사용자 인증 정보를 관리.

    ➡️ 쿠키와 세션은 모두 클라이언트와 서버 간의 상태 정보를 유지하기 위한 기술.

    ➡️ 쿠키 : 클라이언트(브라우저)에 저장되는 키와 값이 들어있는 작은 데이터 파일.

            - 쿠키는 사용자가 웹사이트를 방문할 때 생성되어 웹 브라우저에 저장됩니다.
            - 쿠키는 사용자의 브라우저가 웹 서버에 요청을 보낼 때마다 서버로 전송되므로, 서버는 이 정보를 사용하여 사용자를 식별할 수 있음.


    ➡️ 세션 : 클라이언트가 웹 서버에 접속해있는 상태가 하나의 단위.

            - 서버에 저장됨, 세션은 사용자가 웹사이트에 접속하는 순간 생성.
            - 사용자가 사이트를 이용하는 동안 서버에 저장되어 있음.
            - 사용자가 웹사이트를 나가거나, 브라우저를 닫으면 세션은 종로되고 정보는 삭제.
            - 세션은 쿠키보다 보안성이 높음 => 데이터가 서버에 저장되고, 시용자는 세션 데이터에 직접 접근불가.


    📢 요약
            - 데이터 저장 위치: 쿠키는 클라이언트에, 세션은 서버에 저장됩니다.
            - 보안성: 세션은 쿠키보다 보안성이 높습니다.
            - 생명주기: 쿠키는 설정된 시간동안 데이터가 유지되지만, 세션은 사용자가 웹사이트를 나가거나 브라우저를 닫을 때 종료됩니다.
 */


@RestController // AuthController 클래스를 REST 컨트롤러로 선언.
@RequestMapping("/api") // 이 컨트롤러의 기본 URL 경로를 /api로 설정.
public class AuthController {

    // 인증 헤더의 이름을 상수로 선언, 이 이름은 쿠키와 세션에서 사용됨.
    public static final String AUTHORIZATION_HEADER = "Authorization";


    /*
    /api/create-cookie 경로로 GET 요청이 오면 createCookie 메소드를 실행합니다.
    이 메소드는 addCookie 메소드를 호출하여 Robbie Auth라는 값을 가진 쿠키를 생성하고,
    이 쿠키를 응답에 추가
     */
    @GetMapping("/create-cookie")
    public String createCookie(HttpServletResponse res) {
        addCookie("Robbie Auth", res);

        return "createCookie";
    }

    /*
    /api/get-cookie 경로로 GET 요청이 오면 getCookie 메소드를 실행합니다.
    이 메소드는 요청에서 Authorization 쿠키의 값을 가져와 콘솔에 출력하고,
    이 값을 응답으로 반환
     */
    @GetMapping("/get-cookie")
    public String getCookie(@CookieValue(AUTHORIZATION_HEADER) String value) {
        System.out.println("value = " + value);

        return "getCookie : " + value;
    }

    /*
    /api/create-session 경로로 GET 요청이 오면 createSession 메소드를 실행합니다.
    이 메소드는 세션을 생성하거나 가져오고,
    이 세션에 Authorization이라는 이름으로 Robbie Auth라는 값을 저장
     */
    @GetMapping("/create-session")
    public String createSession(HttpServletRequest req) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 새로운 세션을 생성한 후 반환
        HttpSession session = req.getSession(true);

        // 세션에 저장될 정보 Name - Value 를 추가합니다.
        session.setAttribute(AUTHORIZATION_HEADER, "Robbie Auth");

        return "createSession";
    }


    /*
   /api/get-session 경로로 GET 요청이 오면 getSession 메소드를 실행합니다.
   이 메소드는 세션을 가져오고, 세션에서 Authorization이라는 이름의 값을 가져와 콘솔에 출력하고,
   이 값을 응답으로 반환
    */
    @GetMapping("/get-session")
    public String getSession(HttpServletRequest req) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 null 반환
        HttpSession session = req.getSession(false);

        String value = (String) session.getAttribute(AUTHORIZATION_HEADER); // 가져온 세션에 저장된 Value 를 Name 을 사용하여 가져옵니다.
        System.out.println("value = " + value);

        return "getSession : " + value;
    }



    // 쿠키를 생성하고 응답에 추가하는 작업을 수행, 쿠키의 값은 URL 인코딩되며, 쿠키의 경로는 '/' 로 설정되고, 쿠키의 최대 수명은 30분으로 설정.
    public static void addCookie(String cookieValue, HttpServletResponse res) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, cookieValue); // Name-Value
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}