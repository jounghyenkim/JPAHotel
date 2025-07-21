package com.project.jpaHotel.service;

import com.project.jpaHotel.constant.RoomType;
import com.project.jpaHotel.domain.Cart;
import com.project.jpaHotel.domain.CartRoom;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.CartDetailDto;
import com.project.jpaHotel.dto.CartReservationDto;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.repository.CartRepository;
import com.project.jpaHotel.repository.CartRoomRepository;
import com.project.jpaHotel.repository.MemberRepository;
import com.project.jpaHotel.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartRoomRepository cartRoomRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ReservationService reservationService;

    public Long addCart(ReservationDto reservationDto, String email, Long roomId) {
        Room room = roomRepository.findOne(roomId);

        // [1] 회원 조회
        List<Member> members = memberRepository.findByEmail(email);
        if (members.isEmpty()) {
            throw new EntityNotFoundException("회원 정보를 찾을 수 없습니다: " + email);
        }
        Member member = members.get(0);

        // [2] 장바구니 조회
        List<Cart> carts = cartRepository.findByMemberId(member.getId());
        Cart cart = carts.isEmpty() ? null : carts.get(0);

        // [3] 장바구니가 없으면 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // [4] 이미 장바구니에 같은 roomId가 있는지 확인
        CartRoom savedCartRoom = cartRoomRepository.findByCartIdAndRoomId(cart.getId(), roomId);
        if (savedCartRoom != null) {
            savedCartRoom.addCount(reservationDto.getSearchCount());
            return savedCartRoom.getId();
        } else {
            CartRoom cartRoom = CartRoom.createCartItem(cart, room, reservationDto, member);
            cartRoomRepository.save(cartRoom);
            return cartRoom.getId();
        }
    }
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(Principal principal, HttpSession httpSession){
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        String email =  memberService.loadMemberEmail(principal,httpSession);
        List<Member> members = memberRepository.findByEmail(email);
        Member member = members.get(0);


        List<Cart> carts = cartRepository.findByMemberId(member.getId());
        Cart cart = carts.get(0);
        System.out.println(member.getId() +" member id 조회 CartService");
        if(cart == null){
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartRoomRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }
    @Transactional(readOnly = true)
    public boolean validateCartRoom(Long roomId, String email) {
        List<Member> members = memberRepository.findByEmail(email);
        if (members.isEmpty()) {
            throw new IllegalArgumentException("회원 정보가 없습니다: " + email);
        }
        Member member = members.get(0); // 하나만 있다고 가정

        CartRoom cartRoom = cartRoomRepository.findByRoomId(roomId);
        if (cartRoom == null) {
            throw new IllegalArgumentException("장바구니 상품이 없습니다: ID = " + roomId);
        }

        Member savedMember = cartRoom.getCart().getMember();
        return StringUtils.equals(member.getEmail(), savedMember.getEmail());
    }
    // 장바구니 상품의 수량을 업데이트 하는 메소드입니다.
    public void updateCartRoomCount(Long cartRoomId,int count){
        CartRoom cartRoom = cartRoomRepository.findOne(cartRoomId);
        cartRoom.updateCount(count);
    }

    public void deleteCartRoom(Long cartRoomId){
        CartRoom cartRoom = cartRoomRepository.findOne(cartRoomId);
        cartRoomRepository.deleteCartRoomById(cartRoom.getId());
    }

    public Long reservationCartRoom(List<CartReservationDto> cartReservationDtoList, String email,String name){
        List<ReservationDto> reservationDtoList =new ArrayList<>();
        for (CartReservationDto cartReservationDto : cartReservationDtoList){ // 장바구니 페이지에서 전달받은 주문 상품번호를 이용하여 주문로직으로 전달할 orderDto 객체를만듭니다.
            CartRoom cartRoom = cartRoomRepository.findByRoomId(cartReservationDto.getCartRoomId());

            ReservationDto reservationDto = ReservationDto.reservationDto(cartRoom);
            reservationDtoList.add(reservationDto);
        }
        Long reservationId = reservationService.reservations(reservationDtoList,email,name); // 장바구니에 담은 상품을 주문하도록 주문로직을 호출합니다.

        for (CartReservationDto cartReservationDto: cartReservationDtoList){
            CartRoom cartRoom = cartRoomRepository.findByRoomId(cartReservationDto.getCartRoomId());
            cartRoomRepository.deleteCartRoomById(cartRoom.getId()); // 주문한 상품을 장바구니에서 제거합니다.
        }
        return reservationId;
    }

}
