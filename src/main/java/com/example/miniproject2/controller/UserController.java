package com.example.miniproject2.controller;

import com.example.miniproject2.Entity.CustomUserDetails;
import com.example.miniproject2.Repository.UserRepository;
import com.example.miniproject2.Service.JpaUserDetailsManager;
import com.example.miniproject2.Service.LoginService;
import com.example.miniproject2.jwt.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/users")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsManager manager;
    private final LoginService loginService;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/users/login")
    public String loginForm() {
        return "login-form";
    }
    @GetMapping("/users/my-profile")
    public String myProfile() {
        return "my-profile";
    }
    @GetMapping("/users/register")
    public String signUpForm() {
        return "register-form";
    }
    @PostMapping("/users/register")
    public String signUpRequest(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck,
            @RequestParam("email") String email
    ) {
        if (password.equals(passwordCheck)) {
            UserDetails details = CustomUserDetails
                    .builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .build();
            log.info("password match!");
            manager.createUser(details);
            return "redirect:/users/login";
        }
        log.warn("password does not match...");
        return "redirect:/users/register?error";
    }

    @PostMapping("/login")
    public String login(UserDetails userDetails,
                        RedirectAttributes re
    ) {
        Long userId = loginService.login(userDetails.getUsername(), userDetails.getPassword());
        System.out.println(userDetails.getUsername() + " /" + userDetails.getPassword());

        if (userId != null) { // 로그인 성공

            return "redirect:/items";
        } else { // 로그인 실패
            if(!loginService.IsExistEmail(userDetails.getUsername())){
                re.addFlashAttribute("loginError","존재하지 않는 이메일입니다.");
            }else{
                re.addFlashAttribute("loginError","비밀번호가 일치하지 않습니다.");
            }
            return "redirect:/login"; // 로그인 실패
        }
    }
    // 로그아웃 메소드 추가
    @GetMapping("/users/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 현재 로그인한 사용자 정보를 가져옵니다.
        UserDetails userDetails = (UserDetails) manager.loadUserByUsername(request.getUserPrincipal().getName());
        // 사용자의 토큰을 만료시킵니다.
        String token = jwtTokenUtils.expireToken(request.getHeader("Authorization"));
        // 클라이언트 측에서도 토큰을 삭제하기 위해 토큰을 응답 헤더에 추가합니다.
        response.setHeader("Authorization", "Bearer " + token);
        // 토큰을 삭제한 후, 홈 화면으로 리다이렉트합니다.
        response.sendRedirect("/home");
    }
}
