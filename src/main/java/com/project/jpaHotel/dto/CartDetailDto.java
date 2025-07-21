package com.project.jpaHotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    private Long cartRoomId; // 장바구니 상품 아이디
    private String roomNm; // 상품명
    private int price; // 가격
    private int count; // 수량
    private String imgUrl; // 상품이미지 경로
    public CartDetailDto(Long cartRoomId, String roomNm, int price, int count, String imgUrl){
        this.cartRoomId = cartRoomId;
        this.roomNm = roomNm;
        this.price = price;
        this.imgUrl = imgUrl;
        this.count = count;
    }
}
