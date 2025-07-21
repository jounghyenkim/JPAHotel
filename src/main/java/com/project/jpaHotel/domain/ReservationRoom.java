package com.project.jpaHotel.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

@Entity
@Getter @Setter
public class ReservationRoom extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "reservation_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private int reservationPrice;
    private int count;


    public static ReservationRoom createReservationRoom(Room room, int count){
        ReservationRoom reservationRoom =new ReservationRoom();
        reservationRoom.setRoom(room);
        reservationRoom.setCount(count);
        reservationRoom.setReservationPrice(room.getPrice());
        room.removeStock(count);
        return reservationRoom;
    }
}
