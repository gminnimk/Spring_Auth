package com.sparta.springauth.filter;


/*
✅ Spring Boot 애플리케이션에서 HTTP 요청과 응답을 가로채서 로그를 남기는 필터.

    ➡️ 이 클래스는 HTTP 요청이 들어올 때마다 해당 요청의 URL을 로그로 남기고, 비즈니스 로직이 완료된 후에도 로그를 남깁니다.
       이를 통해 요청과 응답의 흐름을 추적할 수 있습니다.


     📢 Filter : Web 애플리케이션에서 관리되는 영역으로 Client로 부터 오는 요청과 응답에 대해 촤초/최종 단계의 위치이며 이를 통해
                 요청과 응답의 정보를 변경하거나 부가적인 기능을 추가할 수 있음.
 */

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 로깅을 위한 설정으로, LoggingFilter라는 이름의 로거를 생성.
@Slf4j(topic = "LoggingFilter")

// Spring에서 이 클래스를 빈으로 등록.
@Component

// 이 필터의 실행 순서를 지정, 숫자가 낮을수록 먼저 실행.
@Order(1)


// 이 클래스가 Filter 인터페이스를 구현한다고 선언.
public class LoggingFilter implements Filter {

    // doFilter 메소드는 필터의 핵심 기능을 정의. 요청이 필터를 통과할 때마다 이 메서드가 호출.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 전처리
        // ServletRequest를 HttpServletRequest로 캐스팅. 이렇게 하면 HTTP 관련 메서드를 사용할 수 있음.
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI(); // 요청된 URL을 가져옴.
        log.info(url); // URL을 로그에 남김.

        chain.doFilter(request, response); // 다음 필터로 요청과 응답을 전달, 만약 이 필터가 마지막 필터라면, 실제 서블릿이 호출.

        // 후처리
        log.info("비즈니스 로직 완료"); // 비즈니스 로직이 완료된 후 로그를 남김.
    }
}
