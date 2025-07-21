package com.project.jpaHotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartReservationDto {
    private Long cartRoomId;

    private List<CartReservationDto> cartReservationDtoList; // 장바구니에서 여러개의 상품을 주문하므로 CartDto 클래스가 자기 자신을 List로 가지고있도록 합니다
}
