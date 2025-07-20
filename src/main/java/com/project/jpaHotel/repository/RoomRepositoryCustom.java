package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.MainRoomDto;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.dto.RoomFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomRepositoryCustom {
    Page<Room> getAdminRoomPage(ReservationDto reservationDto, Pageable pageable);
    Page<MainRoomDto> getMainRoomPage(ReservationDto reservationDto, Pageable pageable);
    List<MainRoomDto> getMainRoomPages(ReservationDto reservationDto);
}
