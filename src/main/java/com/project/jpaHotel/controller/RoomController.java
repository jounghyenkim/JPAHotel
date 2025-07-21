package com.project.jpaHotel.controller;

import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.dto.RoomFormDto;
import com.project.jpaHotel.service.MemberService;
import com.project.jpaHotel.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final MemberService memberService;
    private final HttpSession httpSession;

    @GetMapping(value = "/admin/room/new")
    public String roomForm(Model model, Principal principal,RoomFormDto roomFormDto) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        model.addAttribute("roomFormDto", roomFormDto);
        return "/room/roomForm";
    }

    @PostMapping(value = "/admin/room/new")
    public String roomNew(@Valid RoomFormDto roomFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("roomImgFile") List<MultipartFile> roomImgFileList, Principal principal) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        if (bindingResult.hasErrors()) {
            return "room/roomForm";
        }
        if (roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 객실 이미지는 필수 입력 값입니다.");
            return "room/roomForm";
        }
        try {
            roomService.saveRoom(roomFormDto, roomImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "객실 등록 중 에러가 발생하였습니다.");
            return "room/roomForm";
        }

        return "redirect:/";
    }
    @GetMapping(value = "/admin/room/{roomId}")
    public String roomDtl(@PathVariable("roomId")Long roomId, Model model, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        httpSession.setAttribute("roomId",roomId);
        System.out.println("roomId = " + roomId);
        try {
            RoomFormDto roomFormDto = roomService.getRoomDtl(roomId);
            model.addAttribute("roomFormDto", roomFormDto);
            return "room/roomForm";
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
        }
        return "room/roomForm";
    }

    @PostMapping(value = "/admin/room/{roomId}")
    public String roomUpdate(@Valid RoomFormDto roomFormDto, BindingResult bindingResult,
                             @RequestParam("roomImgFile") List<MultipartFile> roomImgFileList,
                             Model model, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        if(bindingResult.hasErrors()){
            return "room/roomForm";
        }
        if(roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "room/roomForm";
        }
        try {
            roomService.updateRoom(roomFormDto, roomImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "room/roomForm";
        }
        return "redirect:/"; // 다시 실행 /
    }

    @GetMapping(value = {"/admin/rooms", "/admin/rooms/{page}"})
    public String roomManage(ReservationDto reservationDto, @PathVariable("page") Optional<Integer> page,
                             Model model, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Room> rooms = roomService.getAdminRoomPage(reservationDto, pageable);

        model.addAttribute("name",name);
        model.addAttribute("rooms", rooms);
        model.addAttribute("reservationDto",reservationDto);
        model.addAttribute("maxPage",5);
        return "room/roomMng";
    }

    @GetMapping(value = "/room/{roomId}")
    public String roomDtl(Model model, @PathVariable("roomId")Long roomId, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        RoomFormDto roomFormDto = roomService.getRoomDtl(roomId);

        model.addAttribute("roomFormDto",roomFormDto);
        return "room/roomDtl";
    }
}