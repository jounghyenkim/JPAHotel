package com.project.jpaHotel.dto;

import com.project.jpaHotel.constant.ReservationStatus;
import com.project.jpaHotel.constant.RoomSellStatus;
import com.project.jpaHotel.constant.RoomType;
import com.project.jpaHotel.domain.CartRoom;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDto {
    private Long roomId;
    private String searchDateType; // 조회날짜
    private RoomSellStatus roomSellStatus; // 상태
    private String searchBy; // 조회 유형
    private String searchQuery = ""; // 검색 단어
    private int searchBreakfast;
    @Min(value = 1, message = "최소 1개이상 담아주세요.")
    private int searchCount;
    private RoomType searchRoomType;
    private String searchRoomNm;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchCheckIn ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchCheckOut ;
    private int searchAdultCount ;
    private int searchChildrenCount ;
    private int searchPrice ;
    private ReservationStatus reservationStatus;

    public static ReservationDto reservationDto(CartRoom cartRoom){
        ReservationDto reservationDto =new ReservationDto();
        reservationDto.setRoomId(cartRoom.getRoom().getId());
        reservationDto.setSearchCount(cartRoom.getCount());
        reservationDto.setSearchRoomType(RoomType.valueOf(cartRoom.getType()));
        reservationDto.setSearchCheckIn(cartRoom.getCheckIn());
        reservationDto.setSearchCheckOut(cartRoom.getCheckOut());
        reservationDto.setSearchAdultCount(cartRoom.getAdultCount());
        reservationDto.setSearchBreakfast(cartRoom.getBreakfast());
        reservationDto.setSearchChildrenCount(cartRoom.getChildrenCount());
        reservationDto.setSearchPrice(cartRoom.getPrice());

        return reservationDto;
    }

}
