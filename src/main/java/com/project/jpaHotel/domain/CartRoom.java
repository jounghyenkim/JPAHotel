package com.project.jpaHotel.domain;


import com.project.jpaHotel.dto.ReservationDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class CartRoom extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "cart_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private int count;
    private int adultCount;
    private int childrenCount;

    @Column(name = "checkIn")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "checkOut")
    private LocalDate checkOut;

    @Column(name = "type")
    private String type;
    private int price;
    @Column(name = "breakfast")
    private int breakfast;

    public static CartRoom createCartItem(Cart cart, Room room, ReservationDto reservationDto, Member member){
        CartRoom cartRoom = new CartRoom();
        cartRoom.setCart(cart);
        cartRoom.setCheckIn(reservationDto.getSearchCheckIn());
        cartRoom.setCheckOut(reservationDto.getSearchCheckOut());
        cartRoom.setBreakfast(reservationDto.getSearchBreakfast());
        cartRoom.setPrice(reservationDto.getSearchPrice());
        cartRoom.setAdultCount(reservationDto.getSearchAdultCount());
        cartRoom.setChildrenCount(reservationDto.getSearchChildrenCount());
        cartRoom.setType(String.valueOf(reservationDto.getSearchRoomType()));
        cartRoom.setRoom(room);
        cartRoom.setCount(reservationDto.getSearchCount());
        cartRoom.setMember(member);
        return cartRoom;
    }
    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
