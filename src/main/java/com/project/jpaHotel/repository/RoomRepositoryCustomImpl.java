package com.project.jpaHotel.repository;

import com.project.jpaHotel.constant.RoomSellStatus;
import com.project.jpaHotel.constant.RoomType;
import com.project.jpaHotel.domain.QRoom;
import com.project.jpaHotel.domain.QRoomImg;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.MainRoomDto;
import com.project.jpaHotel.dto.QMainRoomDto;
import com.project.jpaHotel.dto.ReservationDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public RoomRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(RoomSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QRoom.room.roomSellStatus.eq(searchSellStatus);
        //ItemSellStatus null이면 null 리턴 null 아니면 SELL, SOLD 둘중 하나 리턴
    }
    private BooleanExpression regDtsAfter(String searchDateType) { // all, 1d, 1w, 1m 6m
        LocalDateTime dateTime = LocalDateTime.now(); // 현재시간을 추출해서 변수에 대입

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QRoom.room.regTime.after(dateTime);
        //dateTime을 시간에 맞게 세팅 후 시간에 맞는 등록된 상품이 조회하도록 조건값 반환
    }
    private BooleanExpression searchRoomTypeEq(RoomType searchRoomType) {
        return searchRoomType == null ? null : QRoom.room.roomType.eq(String.valueOf(searchRoomType));
    }

    private BooleanExpression roomNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QRoom.room.roomNm.like("%"+searchQuery+"%");
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("roomNm",searchBy)){ // 상품명
            return QRoom.room.roomNm.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){ // 작성자
            return QRoom.room.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Room> getAdminRoomPage(ReservationDto reservationDto, Pageable pageable) {
        QueryResults<Room> results = queryFactory.selectFrom(QRoom.room)
                .where(regDtsAfter(reservationDto.getSearchDateType()),
                        searchSellStatusEq(reservationDto.getRoomSellStatus()),
                        searchByLike(reservationDto.getSearchBy(),reservationDto.getSearchQuery()))
                .orderBy(QRoom.room.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Room> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<MainRoomDto> getMainRoomPage(ReservationDto reservationDto, Pageable pageable) {
        QRoom room = QRoom.room;
        QRoomImg roomImg = QRoomImg.roomImg;
        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainRoomDto> results = queryFactory.select(new QMainRoomDto(room.id, room.roomNm,
                        room.roomDetail,roomImg.imgUrl, room.price.stringValue(),room.totalCount,room.size,room.roomType,room.stockNumber))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(roomImg).join(roomImg.room, room).where(roomImg.repImgYn.eq("Y"))
                .where(roomNmLike(reservationDto.getSearchQuery())
                        , searchRoomTypeEq(reservationDto.getSearchRoomType()))
                .orderBy(room.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainRoomDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<MainRoomDto> getMainRoomPages(ReservationDto reservationDto) {
        return List.of();
    }


}
