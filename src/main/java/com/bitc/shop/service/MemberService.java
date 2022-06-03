package com.bitc.shop.service;

import com.bitc.shop.entity.Member;
import com.bitc.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Transactional : 데이터 베이스의 트랜잭션(백업) 기능을 실행하는 어노테이션
// @RequiredArgsConstructor : Autowired 대신 스프링 프레임워크에서 관리하는 빈으로 등록하기 위한 방법
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
//        이메일을 기반으로 해당 하는 이메일 정보가 있는지 확인, 없을 경우 저장
//        입력된 이메일이 기존에 없으면 무시하고, 있을 경우 예외가 발생하여 save()가 동작하지 않음
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

//    사용자가 존재하는지 확인하는 메서드
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

//    UserDetailsService : 해당 인터페이스는 사용자 입력 정보를 바탕으로UserDetails 객체를 생성하는 역할을 함
//    해당 인터페이스를 상속받고 구현 시 인증 정보를 받을 수 있음
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

//        usernmae : 스프링 시큐리티에서 사용하는 사용자ID
//        password : 스프링 시큐리티에서 사용하는 비밀번호
//        roles : 스프링 시큐리티에서 사용하는 사용자 등급
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
