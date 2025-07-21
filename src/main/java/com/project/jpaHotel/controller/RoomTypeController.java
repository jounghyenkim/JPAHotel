package com.project.jpaHotel.controller;

import com.project.jpaHotel.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomTypeController {
    private final MemberService memberService;
    private final HttpSession httpSession;


    @GetMapping(value = {"/deluxe", "/deluxe/{page}"})
    public String bread(Principal principal, Model model){


        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);

        return "/room/deluxe";
    }
    @GetMapping(value = {"/royal", "/royal/{page}"})
    public String cake(Principal principal, Model model){

        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("maxPage",5);
        return "/room/royal";
    }
    @GetMapping(value = {"/standard", "/standard/{page}"})
    public String salad( Principal principal, Model model){

        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("maxPage",5);
        return "/room/standard";
    }

    @GetMapping(value ={"/sweet", "/sweet/{page}"})
    public String iceCream(Principal principal, Model model){


        String name = memberService.loadMemberName(principal,httpSession);

        model.addAttribute("name",name);
        model.addAttribute("maxPage",5);

        return "/room/sweet";
    }
}
