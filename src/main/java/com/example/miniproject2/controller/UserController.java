package com.example.miniproject2.controller;

import com.example.miniproject2.Entity.CustomUserDetails;
import com.example.miniproject2.Service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/users")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsManager manager;

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

//    @PostMapping("/items")
//    public String login()
}
