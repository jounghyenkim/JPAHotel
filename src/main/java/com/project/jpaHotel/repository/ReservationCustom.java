package com.project.jpaHotel.repository;


import com.project.jpaHotel.domain.Reservation;
import com.project.jpaHotel.dto.ReservationSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationCustom {
    Page<Reservation> getAdminReservationPage(ReservationSearchDto reservationSearchDto, Pageable pageable);
}
