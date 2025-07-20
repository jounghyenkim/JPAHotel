package com.project.jpaHotel.dto;

import com.project.jpaHotel.constant.RoomSellStatus;
import com.project.jpaHotel.domain.Room;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RoomFormDto {
    private Long id;

    @NotBlank(message = "객실명은 필수 입력 값입니다.")
    private String roomNm;
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int price;
    private int size;
    @NotBlank(message = "객실 상세 정보는 필수 입력값입니다.")
    private String roomDetail;
    @NotNull(message = "전체 인원은 필수 입력값입니다.")
    private int totalCount;
    private int stockNumber;
    private String roomType;
    private RoomSellStatus roomSellStatus;

    //
    // itemImg
    private List<RoomImgDto> roomImgDtoList =new ArrayList<>();
    private List<Long> roomImgIds = new ArrayList<>();

    // Model Mapper
    private static ModelMapper modelMapper =new ModelMapper();

    public Room createRoom(){
        // RoomFormDto -> Room 연결
        return modelMapper.map(this,Room.class);
    }
    public static RoomFormDto of(Room room){
        // Room -> RoomFormDto 연결
        return modelMapper.map(room, RoomFormDto.class);
    }
}
