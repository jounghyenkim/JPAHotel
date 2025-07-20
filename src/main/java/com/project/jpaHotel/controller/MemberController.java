package com.project.jpaHotel.controller;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.dto.MemberFormDto;
import com.project.jpaHotel.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String join(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            return "/member/memberForm";
        }
        try {
            Member member = Member.CreateMember(memberFormDto, passwordEncoder);
            memberService.join(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/member/memberForm";
        }
        return "/member/memberLoginForm";
    }


    @GetMapping(value = "/login")
    public String login(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
}
