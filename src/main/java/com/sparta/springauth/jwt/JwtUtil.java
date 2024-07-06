package com.sparta.springauth.jwt;

import com.sparta.springauth.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


// Util 이라는 건 다른 객체에 의존하지 않고 하나의 모듈로서 동작하는 클래스
// 즉, JwtUtil 이라는 건 jwt 관련된 기능들을 가진 클래스.
@Component
public class JwtUtil {


    // ➡️ JWT 데이터

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; // 토큰 앞에 붙히지만 구분하기 위해서 한 칸 뛰움.
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    // ➡️ 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        // JWT 토큰을 생성하고 반환, 토큰에는 사용자의 ID, 권한, 만료 시간, 발급일이 포함.
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }


    // ➡️ JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            // 쿠키 값은 URL 인코딩되어 있을 수 있으므로, URLEncoder.encode 메서드를 사용해 인코딩.
            // Cookie Value에는 공백이 불가능해서 encoding 진행
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");


            // 쿠키를 생성하고, 쿠키의 이름과 값에 각각 AUTHORIZATION_HEADER와 토큰을 설정.
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }


    // ➡️ Cookie에 들어있던 JWT 토큰을 Substring
    public String substringToken(String tokenValue) {

        // StringUtils.hasText 메소드를 사용하여 토큰 값이 null이 아니고, 길이가 0보다 큰지 확인.
        // tokenValue.startsWith(BEARER_PREFIX)를 사용하여 토큰 값이 "Bearer "로 시작하는지 확인.
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {

            // "Bearer " 접두사를 제거하고 토큰 값을 반환합니다. "Bearer "는 7글자이므로,
            // substring(7)을 사용하여 7번째 인덱스부터 문자열을 추출.
            return tokenValue.substring(7);
        }
        // 토큰 값이 null이거나, "Bearer "로 시작하지 않는 경우, 에러 메시지를 로그에 기록하고 NullPointerException을 발생.
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }


    // ➡️ JWT 검증
    public boolean validateToken(String token) {
        try {
            // Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)를 사용하여 JWT 토큰을 파싱하고 검증.
            // 이 메소드는 JWT 토큰이 유효한지 확인하고, 유효하지 않은 경우 예외를 발생.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    // ➡️ JWT에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        // Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()를 사용하여 JWT 토큰을 파싱하고, 토큰의 내용(Claims)을 반환.
        // 이 메소드는 JWT 토큰에서 사용자의 식별자(ID), 권한, 만료 시간 등의 정보를 추출하는데 사용.
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    // ➡️ 사용자의 요청에서 JWT 토큰을 추출하는 역할
    // 사용자가 로그인을 하면 서버는 JWT 토큰을 생성하고 이를 쿠키에 저장하여 응답.
    // 이후 사용자의 모든 요청에는 이 JWT 토큰이 포함되어 서버로 전송됨.
    // 서버는 이 JWT 토큰을 이용하여 사용자의 인증 상태를 확인하고, 요청을 처리.
    public String getTokenFromRequest(HttpServletRequest req) {
        // HttpServletRequest 객체를 인자로 받아, 요청에 포함된 쿠키에서 JWT 토큰을 추출하여 반환.

        // 요청에서 모든 쿠키를 배열 형태로 가져옴. 쿠키가 없으면 null을 반환.
        Cookie[] cookies = req.getCookies();
        // 쿠키 배열이 null이 아닌지 확인.
        if (cookies != null) {
            // 각 쿠키를 순회하며, 쿠키의 이름이 AUTHORIZATION_HEADER와 같은지 확인.
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) { // AUTHORIZATION_HEADER는 JWT 토큰이 저장된 쿠키의 이름.

                    // 쿠키 값은 URL 인코딩되어 있을 수 있으므로, URLDecoder.decode 메서드를 사용해 디코딩.
                    // 디코딩 과정에서 UnsupportedEncodingException이 발생할 수 있으며, 이 경우 null을 반환.
                    try {
                        // Encode 되어 넘어간 Value 다시 Decode. 해당 쿠키를 찾으면 cookie.getValue()로 쿠키 값을 가져옴.
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null; // 쿠키 배열이 null이거나, JWT 토큰이 저장된 쿠키를 찾지 못한 경우 null을 반환.
    }
}