package com.bitc.shop.service;

import com.bitc.shop.dto.MemberFormDto;
import com.bitc.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// @SpringBootTest : 해당 클래스가 JUnit을 사용하는 테스트 클래스 파일임을 나타내는 어노테이션
// @Transactional : 테스트에서 트랜잭션을 사용 시 테스트 사용 후 데이터베이스에 저장된 내용을 롤백함
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

//   테스트용 사용자 DB 생성
    public Member createMember() {
        MemberFormDto memberFromDto = new MemberFormDto();
        memberFromDto.setEmail("test@gmail.com");
        memberFromDto.setName("테스터01");
        memberFromDto.setAddress("부산광역시 부산진구 전포대로");
        memberFromDto.setPassword("bitc1234");

        return Member.createMember(memberFromDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
//        테스트용 회원 데이터 생성(기존 저장된 사용자 데이터)
        Member member = createMember();

        Member savedMember = memberService.saveMember(member);

//        assertEquals() : JUnit에서 지원하는 메소드로 값을 비교할때 사용하는 메서드
//        저장된 데이터와 저장할 데이터를 비교
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}
