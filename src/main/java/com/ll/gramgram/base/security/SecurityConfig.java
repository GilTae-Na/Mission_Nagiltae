package com.ll.gramgram.base.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//스프링 시큐리티 설정
//커스텀 객체를 만드는 것만으로 기본 설정이 꺼진다

@Configuration
@EnableWebSecurity
//페이지 진입 전체 로그인 여부 체크
// Spring Security를 사용하는 웹 어플리케이션에서
// 보안 설정을 활성화하기 위함
//필터 체인을 구성하기 위한 설정을 활성화
@EnableMethodSecurity(prePostEnabled = true)
//메서드 레벨의 보안 설정을 활성화
//메서드에서 @PreAuthorize, @PostAuthorize 등의 애노테이션을
//사용하여 메서드 호출에 대한 보안 설정을 구현
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2AccessTokenResponseClient oAuth2AccessTokenResponseClient;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/usr/member/login")
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/usr/member/login")
                                .tokenEndpoint(t -> t
                                        .accessTokenResponseClient(oAuth2AccessTokenResponseClient)
                                )
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/usr/member/logout")
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
