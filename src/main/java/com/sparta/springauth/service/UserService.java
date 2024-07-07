package com.sparta.springauth.service;

import com.sparta.springauth.dto.LoginRequestDto;
import com.sparta.springauth.dto.SignupRequestDto;
import com.sparta.springauth.entity.User;
import com.sparta.springauth.entity.UserRoleEnum;
import com.sparta.springauth.jwt.JwtUtil;
import com.sparta.springauth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


/*
✅ 사용자 등록과 관련된 로직을 처리하는 서비스 계층의 클래스.

    ➡️ 사용자 이름과 이메일의 중복 검사, 비밀번호의 암호화, 사용자 역할의 설정 들을 수행.
 */


@Service // 이 클래스가 서비스 계층의 클래스임을 나타내는 스프링 어노테이션, 해당 어노태이션을 사용하면 스프링이 이 클래스를 관리.
public class UserService {


    private final UserRepository userRepository; // 사용자 정보를 DB에서 조회하거나 저장하는 인스턴스
    private final PasswordEncoder passwordEncoder; // 비밀번호를 암호화하는 데 사용되는 인스턴스
    private final JwtUtil jwtUtil; // jwtUtil 클래스의 인스턴스를 참조하는 필드. (JwtUtil 클래스는 JWT를 생성하고 검증하는 기능)

    // 아래 생성자를 통해 UserRepository와 PasswordEncoder, JwtUtil 주입.
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    // ADMIN_TOKEN
    // 관리자 등록을 위한 토큰, 사용자가 관리자로 등록혀면 이 토큰을 제공 해야함.
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    // signup 메서드는 사용자 등록을 처리, SignupRequestDto를 인자로 받아 사용자 이름과 비밀번호를 가져옴.
    // 비밀번호는 passwordEncoder를 사용해 암호화!
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());


        // 회원 중복 확인
        // userRepository.findByUsername(username)을 호출하여 동일한  사용자 이름을 가진 사용자가 있는지 확인.
        // 만약 동일한 사용자 이름을 가진 사용자가 있을 시, 예외를 발동.
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }


        // email 중복확인
        // userRepository.findByEmail(email)을 호출하여 동일한 이메일을 가진 사용자가 있는지 확인.
        // 만약 동일한 이메일을 가진 사용자가 있을 시, 예외를 발동.
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }


        // 사용자 ROLE 확인
        // 사용자의 역할을 설정, 기본 사용자의 역할을 USER로 설정되어 있지만,
        // requestDto.isAdmin()이 true이고 관리자 토큰이 일치하면 역할을 ADMIN으로 변경.
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }


        // 사용자 등록
        // User 객체를 생성하고 userRepository.save(user)를 호출하여 사용자를 DB에 저장.
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }


    // 로그인 처리를 Filter 에서 실행하므로 이에 따라 주석처리.
//    // login 메서드는 사용자 로그인을 처리, LoginRequestDto를 인자로 받아 사용자 이름과 비밀번호를 가져와서 검증하고, JWT를 생성하여 쿠키에 저장하는 기능.
//    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
//        String username = requestDto.getUsername();
//        String password = requestDto.getPassword();
//
//
//        // 사용자 확인
//        // userRepository.findByUsername(username)을 호출하여 사용자 이름으로 사용자를 조회.
//        // 만약 해당 사용자가 없다면, 예외를 발생시킴.
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//
//
//        // 비밀번호 확인
//        // 입력받은 비밀번호와 저장된 비밀번호를 비교.
//        // passwordEncoder.matches(password, user.getPassword())를 호출하여 입력받은 비밀번호와 저장된 비밀번호가 일치하는지 확인.
//        // 만약 비밀번호가 일치하지 않다면, 예외를 발생.
//        if(!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//
//        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
//        // jwtUtil.createToken(user.getUsername(), user.getRole())을 호출하여 JWT를 생성,
//        // 생성된 JWT는 jwtUtil.addJwtToCookie(token, res)를 호출하여 쿠키에 저장되고, 이 쿠키는 HttpServletResponse 객체에 추가.
//        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
//        jwtUtil.addJwtToCookie(token, res);
//    }
}