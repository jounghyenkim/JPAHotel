package com.project.jpaHotel.controller;

import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.MainRoomDto;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.dto.RoomFormDto;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class ReservationController {
    private final MemberService memberService;
    private final ReservationService reservationService;
    private final RoomService roomService;

    @GetMapping(value = {"/reservations","/reservations/{page}"})
    public String Reservation1(@PathVariable("page")Optional<Integer> page, Model model,
                               Principal principal, HttpSession httpSession,ReservationDto reservationDto){
        if (reservationDto.getSearchQuery() == null){
            reservationDto.setSearchQuery("");
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainRoomDto> rooms = roomService.getMainRoomPages(reservationDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);

        model.addAttribute("name",name);
        model.addAttribute("reservationDto",reservationDto);
        model.addAttribute("rooms",rooms);
        model.addAttribute("maxPage",5);
        return "/reservation/reservation1";
    }
    @GetMapping(value = "/reservation2/{roomId}")
    public String Reservation2(@PathVariable("roomId")Long roomId,ReservationDto reservationDto, Optional<Integer> page, Model model,
                               Principal principal,HttpSession httpSession){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        if(reservationDto.getSearchQuery() == null)
        {
            reservationDto.setSearchQuery("");
        }
        String name = memberService.loadMemberName(principal,httpSession);
        RoomFormDto roomFormDto = roomService.getRoomDtl(roomId);
        Page<MainRoomDto> rooms = roomService.getMainRoomPages(reservationDto, pageable);
        httpSession.setAttribute("roomId",roomId);

        model.addAttribute("name",name);
        model.addAttribute("roomFormDto",roomFormDto);
        model.addAttribute("reservationDto",reservationDto);
        model.addAttribute("rooms",rooms);

        return "/reservation/reservation2";
    }
    @PostMapping(value = "/reservationOk")
    public @ResponseBody
    ResponseEntity reservation(@RequestBody ReservationDto reservationDto,
                         BindingResult bindingResult,
                         Principal principal, Reservation reservation, HttpSession httpSession, Room room) throws Exception {

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        // 로그인 정보 -> Spring Security
        // principal.getName() (현재 로그인된 정보)
        String email = memberService.loadMemberEmail(principal,httpSession);
        String name = memberService.loadMemberName(principal,httpSession);
        try {
           reservationService.ReservationOk(email,name,reservationDto);

        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
    }
}
