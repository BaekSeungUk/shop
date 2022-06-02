package com.bitc.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@EnableWebSecurity : SpringSecurityFilterChain이 자동으로 포함되어 스프링 시큐리티 설정을 변경할 수 있음
//스프링 시큐리티 설정을 변경하기 위해서 WebSecurityConfigurerAdapter 클래스를 상속받아 사용함
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    스프링 시큐리티를 통해서 접속하는 웹페이지에 관련된 정보를 수정할 수 있음
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    }
    
//    계정의 비밀번호를 암호화함
//    PasswordEncoder : 비밀번호를 안전하게 저장하기 위해서 Spring Security 에서 지원하는 단반향 암호화 인터페이스
//    메서드 종류 : encode(단방향 암호화), matches(암호화된 비밀번호와 암호화되지 않은 비밀번호가 같은지 확인), upgradeEncoding(암호화된 비밀번호를 다시 암호활 경우)
//    암호화 방식 : BcryptPasswordEncoder, Argon2PasswordEncoder, Pbkdf2PasswordEncoder, SCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
