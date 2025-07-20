package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.repository.MemberRepository;
import com.project.jpaHotel.repository.ReservationRepository;
import com.project.jpaHotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RoomRepository roomRepository;
    public void ReservationOk( String email, String name, ReservationDto reservationDto){ // 예약 로직
        List<Member> members = memberRepository.findByEmail(email);
        Room room = roomRepository.findOne(reservationDto.getRoomId());
        Member member = members.get(0);
        System.out.println("member = " + member.getName());
        Reservation reservation = Reservation.createReservation(member,reservationDto,email,name,room);
        reservationRepository.save(reservation);
    }


    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    public List<Reservation> findByMemberId(Long memberId){
        return reservationRepository.findByMemberId(memberId);
    }

    public Reservation findOne(Long reservationId){
        return reservationRepository.findOne(reservationId);
    }
    public void delete(Long id){
        reservationRepository.deleteByReservationId(id);
    }
    public void deleteAll(){
        reservationRepository.deleteAll();;
    }
}
