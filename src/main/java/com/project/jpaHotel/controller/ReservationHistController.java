package com.project.jpaHotel.controller;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.dto.ReservationSearchDto;
import com.project.jpaHotel.service.MemberService;
import com.project.jpaHotel.service.ReservationService;
import com.project.jpaHotel.service.RoomService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/room")
@RequiredArgsConstructor
public class ReservationHistController {
    private final ReservationService reservationService;
    private final MemberService memberService;

    @GetMapping( value = "/reservationHist")
    public String reservationHist(Model model, HttpSession httpSession,
                                  Principal principal){

        String name = memberService.loadMemberName(principal,httpSession);
        List<Reservation> reservationList =reservationService.getmemberIdList(principal,httpSession);
        System.out.println(reservationList+ ": reservationList");


        model.addAttribute("name",name);
        model.addAttribute("List",reservationList);

        return "/hist/reservationHist";
    }
    @GetMapping(value = "/reservationDtl/{reservationId}")
    public String reservationDtl(@PathVariable("reservationId")Long reservationId,Principal principal,
                                 HttpSession httpSession,Model model){
        String name =memberService.loadMemberName(principal,httpSession);
        Member member =memberService.findMember(httpSession,principal);

        Reservation reservation=reservationService.findOne(reservationId);

        model.addAttribute("role",member.getRole());
        model.addAttribute("reservation",reservation);
        model.addAttribute("name",name);
        return "/hist/reservationDtl";
    }

    @GetMapping( value = {"/adminReservationHist","/adminReservationHist/{page}"})
    public String adminReservationHist(Model model, HttpSession httpSession,
                                       Principal principal, @PathVariable("page") Optional<Integer> page, ReservationSearchDto reservationSearchDto){
        if(reservationSearchDto.getSearchQuery() == null || reservationSearchDto.getSearchQuery().isEmpty())
        {
            reservationSearchDto.setSearchQuery("");

        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );

        Page<Reservation> reservations = reservationService.getAdminReservationPage(reservationSearchDto,pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
//        model.addAttribute("List",reservationService.getAll());
        model.addAttribute("reservationSearchDto",reservationSearchDto);
        model.addAttribute("reservations",reservations);
        model.addAttribute("maxPage", 5);
        return "/hist/adminReservation";
    }

    @PostMapping( value = "/deleteAll")
    public String deleteAll(){
        reservationService.deleteAll();
        return "redirect:/";
    }
    @PostMapping(value = "/deleteById/{reservationId}")
    public @ResponseBody
    ResponseEntity deleteById(@PathVariable("reservationId")Long reservationId) throws Exception {
        try {
            reservationService.delete(reservationId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(reservationId, HttpStatus.OK);
    }
}
