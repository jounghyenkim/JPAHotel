package com.project.jpaHotel.controller;

import com.nimbusds.jose.crypto.impl.PRFParams;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;

    @RequestMapping(value = "/")
    public String main(HttpSession httpSession, Principal principal, Model model){

        String name = memberService.loadMemberName(principal, httpSession);

        model.addAttribute("name", name);
        return "main";
    }

}
