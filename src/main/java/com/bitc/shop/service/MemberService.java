package com.bitc.shop.service;

import com.bitc.shop.entity.Member;
import com.bitc.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Transactional : 데이터 베이스의 트랜잭션(백업) 기능을 실행하는 어노테이션
// @RequiredArgsConstructor : Autowired 대신 스프링 프레임워크에서 관리하는 빈으로 등록하기 위한 방법
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

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
}
