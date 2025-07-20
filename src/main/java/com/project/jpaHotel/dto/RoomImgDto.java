package com.project.jpaHotel.dto;

import com.project.jpaHotel.domain.RoomImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;


@Getter
@Setter
public class RoomImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();
    public static RoomImgDto of(RoomImg roomImg){return modelMapper.map(roomImg,RoomImgDto.class);
    }
}
