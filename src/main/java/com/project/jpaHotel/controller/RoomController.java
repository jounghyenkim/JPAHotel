package com.project.jpaHotel.controller;

import com.project.jpaHotel.dto.RoomFormDto;
import com.project.jpaHotel.service.MemberService;
import com.project.jpaHotel.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final MemberService memberService;
    private final HttpSession httpSession;

    @GetMapping(value = "/admin/room/new")
    public String roomForm(Model model, Principal principal) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        model.addAttribute("roomFormDto", new RoomFormDto());
        return "/room/roomForm";
    }

    @PostMapping(value = "/admin/room/new")
    public String itemNew(@Valid RoomFormDto roomFormDto, BindingResult bindingResult, Model model,
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
}