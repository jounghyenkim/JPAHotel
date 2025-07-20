package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.domain.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {
    @Autowired MemberService memberService;
    @Autowired ReservationService reservationService;
    @Autowired RoomService roomService;
    @Test
    @Rollback(value = false)
    public void 호텔예약() throws Exception{
        // given
        Member member =new Member();
        Member member1 =new Member();
        Reservation reservation = new Reservation();
        Reservation reservation1 = new Reservation();
        member.setEmail("test222");
        member.setName("김정현");

        member1.setName("김수현");
        member1.setEmail("test333");
        reservation.setMember(member);
        reservation.setName(member.getName());
        reservation.setEmail(member.getEmail());
        reservation.setBreakfast(2);
        reservation.setAdultCount(2);
        reservation.setCheckIn(LocalDate.now());
        reservation.setCheckOut(LocalDate.now());

        reservation1.setMember(member1);
        reservation1.setName(member1.getName());
        reservation1.setEmail(member1.getEmail());
        reservation1.setBreakfast(2);
        reservation1.setAdultCount(2);
        reservation1.setCheckIn(LocalDate.now());
        reservation1.setCheckOut(LocalDate.now());


        Room room =new Room();
        room.setRoomDetail("스위트룸 입니다.");
        room.setRoomNm("스위트룸");
        room.setPrice(1000);
        room.setStockNumber(10);

        // when

        memberService.join(member);
        memberService.join(member1);


        // then
        List<Reservation> reservations = reservationService.findAll();
        for (Reservation reservation2 : reservations) {
            System.out.println("reservation1 = " + reservation2.getName());
        }

        List<Reservation> findMember = reservationService.findByMemberId(member.getId());
        for (Reservation reservation2 : findMember) {
            System.out.println("reservation1 = " + reservation2.getName());
            reservationService.delete(reservation2.getId());
            System.out.println("삭제후 = " + reservation2.getName());
        }

    }
}