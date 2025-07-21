package com.project.jpaHotel.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room_img")
@Getter @Setter
public class RoomImg extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "room_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    public void updateRoomImg(String oriImgName,String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
