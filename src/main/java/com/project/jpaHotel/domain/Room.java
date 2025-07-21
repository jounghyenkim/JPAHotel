package com.project.jpaHotel.domain;

import com.project.jpaHotel.constant.RoomSellStatus;
import com.project.jpaHotel.dto.RoomFormDto;
import com.project.jpaHotel.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Room extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;
    private String roomNm;
    private int price;
    private int size;
    private String roomDetail;
    private int stockNumber;
    private int totalCount;
    private String roomType;
    @Enumerated(EnumType.STRING)
    private RoomSellStatus roomSellStatus;


    public void updateRoom(RoomFormDto roomFormDto){
        this.roomNm = roomFormDto.getRoomNm();
        this.price =roomFormDto.getPrice();
        this.size = roomFormDto.getSize();
        this.roomType = roomFormDto.getRoomType();
        this.roomDetail = roomFormDto.getRoomDetail();
        this.totalCount = roomFormDto.getTotalCount();
        this.roomSellStatus = roomFormDto.getRoomSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber; // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구합니다.
        if (restStock<0){
            // 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생시킵니다.
            throw new OutOfStockException("상품의 재고가 부족 합니다. 현재 재고 수량: " + this.stockNumber);
        }
        this.stockNumber =restStock; // 주문후 남은 재고 수량을 상품의 현재 재고 값으로 할당합니다.
    }

}
