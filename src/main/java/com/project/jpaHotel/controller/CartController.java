package com.project.jpaHotel.controller;

import com.project.jpaHotel.domain.CartRoom;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.dto.CartDetailDto;
import com.project.jpaHotel.dto.CartReservationDto;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.service.CartRoomService;
import com.project.jpaHotel.service.CartService;
import com.project.jpaHotel.service.MemberService;
import com.project.jpaHotel.service.RoomService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final CartRoomService cartRoomService;
    @PostMapping(value = "/cart")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid ReservationDto reservationDto,
                         BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {

            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = memberService.loadMemberEmail(principal,httpSession);
        Long cartItemId;
        Long roomId = (Long) httpSession.getAttribute("roomId");

        try {
            cartItemId = cartService.addCart(reservationDto, email,roomId);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model, ReservationDto reservationDto, HttpSession httpSession){
        Member member= memberService.findMember(httpSession,principal);


        List<CartRoom> cartRoomList = cartRoomService.findByMemberId(member.getId());
//        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal,httpSession);


        model.addAttribute("cartRoom", cartRoomList);
        model.addAttribute("reservationDto", reservationDto);
//        model.addAttribute("cartItems", cartDetailDtoList);
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        return "/cart/cartList";

    }

    @PostMapping(value = "/cart/reservations")
    public @ResponseBody ResponseEntity reservationCartRoom (@RequestBody CartReservationDto cartReservationDto, Principal principal,
                                                       HttpSession httpSession,ReservationDto reservationDto){
        String email = memberService.loadMemberEmail(principal,httpSession);
        String name = memberService.loadMemberName(principal,httpSession);
        List<CartReservationDto> cartReservationDtoList = cartReservationDto.getCartReservationDtoList();


        if (cartReservationDtoList == null || cartReservationDtoList.isEmpty()){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요",HttpStatus.FORBIDDEN);
        }
        for (CartReservationDto cartReservationDto1 : cartReservationDtoList){
            if (!cartService.validateCartRoom(cartReservationDto1.getCartRoomId(),email)){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }
        Long roomId = cartService.reservationCartRoom(cartReservationDtoList,email,name);
        return new ResponseEntity<Long>(roomId,HttpStatus.OK);
    }

}
