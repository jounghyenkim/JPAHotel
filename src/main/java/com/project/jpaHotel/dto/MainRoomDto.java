package com.project.jpaHotel.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MainRoomDto {
    private Long id;
    private String itemNm;
    private String itemDetail;
    private Integer size;
    private String type;
    private Integer totalCount;
    private int stockNumber;
    private String imgUrl;
    private String price;
    @QueryProjection //Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록  활용
    public MainRoomDto(Long id, String itemNm, String itemDetail, String imgUrl, String price, Integer size, Integer totalCount, String type, int stockNumber){
        this.id = id;
        this.itemNm = itemNm;
        this.type=type;
        this.size=size;
        this.stockNumber= stockNumber;
        this.totalCount=totalCount;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
