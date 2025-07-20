package com.project.jpaHotel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.jpaHotel.constant.ReservationStatus;
import com.project.jpaHotel.dto.ReservationDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Reservation{

    @Id @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private int breakfast;
    private int adultCount;
    private int childrenCount;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int price;
    private int stockNumber;
    private String email;
    private String name;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



    public static Reservation createReservation(Member member, ReservationDto reservationDto,String email,String name,Room room){
        Reservation reservation =new Reservation();
        reservation.setMember(member);
        reservation.setEmail(email);
        reservation.setBreakfast(reservationDto.getSearchBreakfast());
        reservation.setCheckIn(reservationDto.getSearchCheckIn());
        reservation.setCheckOut(reservationDto.getSearchCheckOut());
        reservation.setName(name);
        reservation.setAdultCount(reservationDto.getSearchAdultCount());
        reservation.setChildrenCount(reservationDto.getSearchChildrenCount());
        reservation.setPrice(reservationDto.getSearchPrice());
        reservation.setType(String.valueOf(reservationDto.getSearchRoomType()));
        reservation.setStockNumber(reservationDto.getSearchCount());
        reservation.setReservationStatus(ReservationStatus.RESERVATION);
        reservation.setReservationDate(LocalDateTime.now());
        room.removeStock(reservationDto.getSearchCount());
        return reservation;
    }



}
