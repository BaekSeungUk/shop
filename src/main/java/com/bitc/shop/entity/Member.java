package com.bitc.shop.entity;

import com.bitc.shop.constant.Role;
import com.bitc.shop.dto.MemberFormDto;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Data
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    기본 설정을 사용하여 테이블의 컬럼으로 사용함
    private String name;

//    컬럼에 unique 속성을 활성화, 다른 컬럼의 내용과 중복되는 내용이 존재할 수 없음
    @Column(unique = true)
    private String email;

    private String password;

    private String address;
    
    @Enumerated(EnumType.STRING)
    private Role role;

//    회원 가입 시 사용자 생성을 위한 메서드
    public static Member createMember(MemberFormDto memberFromDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFromDto.getName());
        member.setEmail(memberFromDto.getEmail());
        member.setAddress(memberFromDto.getAddress());
//        생성된 멤버의 등급을 설정
        member.setRole(Role.USER);

//        비밀번호를 암호화
        String password = passwordEncoder.encode(memberFromDto.getPassword());
        member.setPassword(password);

        return member;
    }
}
