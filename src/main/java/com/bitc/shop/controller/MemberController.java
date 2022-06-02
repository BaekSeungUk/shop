package com.bitc.shop.controller;

import com.bitc.shop.dto.MemberFormDto;
import com.bitc.shop.entity.Member;
import com.bitc.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberForm";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
//        사용자 정보 입력 시 validatecheck 를 진행함
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

//        사용자 정보 DB에 저장
        try {
//            사용자 정보 생성, PasswordEncoder를 통해서 비밀번호 암호화
            Member member = Member.createMember(memberFormDto, passwordEncoder);
//            사용자 정보 DB에 저장
            memberService.saveMember(member);
        }
        catch (IllegalStateException e) {
//            이미 사용자 정보가 있을 경우 오류 발생
            model.addAttribute("errorMessage", e.getMessage());
//            현재 페이지를 다시 출력
            return "member/memberForm";
        }

        return "redirect:/";
    }

}
