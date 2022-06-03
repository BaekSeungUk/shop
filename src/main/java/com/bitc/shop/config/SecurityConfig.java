package com.bitc.shop.config;

import com.bitc.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@EnableWebSecurity : SpringSecurityFilterChain이 자동으로 포함되어 스프링 시큐리티 설정을 변경할 수 있음
//스프링 시큐리티 설정을 변경하기 위해서 WebSecurityConfigurerAdapter 클래스를 상속받아 사용함
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;
//    스프링 시큐리티를 통해서 접속하는 웹페이지에 관련된 정보를 수정할 수 있음
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // form 데이터를 통해서 로그인
        http.formLogin()
//                로그인 페이지 설정
                .loginPage("/members/login")
//                로그인 성공시 자동 이동될 페이지
                .defaultSuccessUrl("/")
//                로그인 시 사용할 ID
                .usernameParameter("email")
//                로그인 실패시 보여줄 페이지
                .failureUrl("/members/login/error")
                .and()
//                로그아웃 기능 사용
                .logout()
//                로그아웃 페이지 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
//                로그아웃 시 이동할 페이지
                .logoutSuccessUrl("/");

//        스프링 시큐리티를 사용한 인증 처리에 HttpServletRequest를 사용
        http.authorizeRequests()
//                mvcMatchers : 주소를 매칭함
//                permitAll : 모든 사용 권한을 허락
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
//                hasRole : 지정한 Role을 가지고 있는 사용자만 사용 권한 허락
                .mvcMatchers("/admin/**").hasRole("ADMIN")
//                anyRequest : 모든 요청
//                authenticated : 사용 인증이 된 경우에만 사용 허락
                .anyRequest().authenticated();
        
//        비인가 사용자가 리소스에 접근햇을 경우 처리 방법 설정
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        스프링 시큐리티에서는 세션을 사용하지 않고 AuthenticationManagerBuilder이 사용자 인증을 관리함
//        사용자 확인 시 사용하는 서비스를 등록하고, 비밀번호 암호화 방식을 설정
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
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
