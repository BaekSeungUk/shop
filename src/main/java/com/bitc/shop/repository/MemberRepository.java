package com.bitc.shop.repository;

import com.bitc.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
//    회원 가입 시 동일한 이메일이 존재하는지 여부 확인
    Member findByEmail(String email);
}
