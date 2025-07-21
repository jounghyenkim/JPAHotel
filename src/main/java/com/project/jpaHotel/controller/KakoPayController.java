package com.project.jpaHotel.controller;

import com.project.jpaHotel.dto.ReadyResponse;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.service.KakaoPayService;
import com.project.jpaHotel.service.MemberService;
import com.project.jpaHotel.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;


@Controller
@RequestMapping(value = "/kakao")
@RequiredArgsConstructor

public class KakoPayController {

private final ReservationService reservationService;
private final KakaoPayService kakaoPayService;
private final MemberService memberService;
//private final CartService cartService;

    // 카카오페이결제 요청
    @GetMapping("/pay/{roomId}")
    public @ResponseBody ReadyResponse payReady(@PathVariable("roomId")Long roomId, ReservationDto reservationDto, Model model, HttpSession httpSession) {
        // 카카오 결제 준비하기	- 결제요청 service 실행
        model.addAttribute("reservationDto",reservationDto);
        httpSession.setAttribute("reservationDto",reservationDto);
        httpSession.setAttribute("roomId",roomId);
        ReadyResponse readyResponse = kakaoPayService.payReady(roomId,reservationDto);
        return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)

    }

    // 결제승인요청
    @GetMapping(value = "/pay/completed")
    public String  approveResponse(Principal principal, HttpSession httpSession) throws Exception {
        ReservationDto reservationDto = (ReservationDto) httpSession.getAttribute("reservationDto");
        String email = memberService.loadMemberEmail(principal,httpSession);
        String name = memberService.loadMemberName(principal,httpSession);


        reservationService.ReservationOk(email,name,reservationDto);
        return "redirect:/";
    }
    // 결제 취소시 실행 url
    @GetMapping("/pay/cancel")
    public String payCancel() {
        System.out.println("취소");
        return "redirect:/";
    }

    // 결제 실패시 실행 url
    @GetMapping("/pay/fail")
    public String payFail() {
        System.out.println("실패");
        return "redirect:/";
    }

}
