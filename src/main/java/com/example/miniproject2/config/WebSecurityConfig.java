package com.example.miniproject2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> authHttp
                                .requestMatchers(
                                        "/no-auth",
                                        "/users/login",
                                        "/users/register",
                                        "/items/**"
                                ).permitAll()
//                                .requestMatchers(
//                                        "/",
//                                        "/users/register"
//                                )
//                                .anonymous()
//                                .anyRequest()
//                                .authenticated()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/users/login")
                                .defaultSuccessUrl("/users/my-profile")
                                .failureUrl("/users/login?fail")
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/users/login")
                )
        ;
        return http.build();
    }

    //    @Bean
    // 사용자 관리를 위한 인터페이스 구현체 Bean
    public UserDetailsManager userDetailsManager(
            PasswordEncoder passwordEncoder
    ) {
        // 임시 User
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder.encode("password"))
                .build();
        // Spring 에서 미리 만들어놓은 사용자 인증 서비스
        return new InMemoryUserDetailsManager(user1);
    }

    @Bean
    // 비밀번호 암호화를 위한 Bean
    public PasswordEncoder passwordEncoder(){
        // 기본적으로 사용자 비밀번호는 해독가능한 형태로 데이터베이스에
        // 저장되면 안된다. 그래서 기본적으로 비밀번호를 단방향 암호화 하는
        // 인코더를 사용한다.
        return new BCryptPasswordEncoder();
    }

}
