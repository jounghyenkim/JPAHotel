package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.domain.ReservationRoom;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.dto.ReservationSearchDto;
import com.project.jpaHotel.repository.MemberRepository;
import com.project.jpaHotel.repository.ReservationRepository;
import com.project.jpaHotel.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
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

        Reservation reservation = Reservation.createReservation(member,reservationDto,email,name,room);
        reservationRepository.save(reservation);
    }

    public Long reservations(List<ReservationDto> reservationDtoList, String email,String name){
        List<Member> members = memberRepository.findByEmail(email);
        Member member = members.get(0);
        List<ReservationRoom> reservationRoomList = new ArrayList<>();

        Reservation reservation =new Reservation();
        for(ReservationDto reservationDto : reservationDtoList){
            Room room = roomRepository.findOne(reservationDto.getRoomId());
            ReservationRoom reservationRoom = ReservationRoom.createReservationRoom(room,reservationDto.getSearchCount());
            reservationRoomList.add(reservationRoom);
            reservation = Reservation.createReservation(member,reservationDto,email,name,room);
        }

        reservationRepository.save(reservation);

        return reservation.getId();
    }

    public List<Reservation> getmemberIdList(Principal principal, HttpSession httpSession) {

        String email = memberService.loadMemberEmail(principal,httpSession);

        List<Member> members =memberRepository.findByEmail(email);
        Member member = members.get(0);

        System.out.println(member.getId() + " member1의 아이디");

        return this.reservationRepository.findByMemberId(member.getId());
    }

    @Transactional(readOnly = true)
    public Page<Reservation> getAdminReservationPage(ReservationSearchDto reservationSearchDto, Pageable pageable){
        return reservationRepository.getAdminReservationPage(reservationSearchDto,pageable);
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
