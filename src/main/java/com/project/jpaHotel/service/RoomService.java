package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.domain.RoomImg;
import com.project.jpaHotel.dto.MainRoomDto;
import com.project.jpaHotel.dto.ReservationDto;
import com.project.jpaHotel.dto.RoomFormDto;
import com.project.jpaHotel.dto.RoomImgDto;
import com.project.jpaHotel.repository.RoomImgRepository;
import com.project.jpaHotel.repository.RoomRepository;
import com.project.jpaHotel.repository.RoomRepositoryCustomImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomImgService roomImgService;
    private final RoomImgRepository roomImgRepository;
    private final RoomRepositoryCustomImpl roomRepositoryCustom;

    @Transactional
    public Long saveRoom(RoomFormDto roomFormDto, List<MultipartFile> roomImgFilsList) throws Exception{
        // 객실 등록
        Room room = roomFormDto.createRoom();
        roomRepository.save(room);
        // 이미지 등록
        for (int i =0 ; i<roomImgFilsList.size(); i++){
            RoomImg roomImg =new RoomImg();
            roomImg.setRoom(room);
            if (i == 0){
                roomImg.setRepImgYn("Y");
            }
            else{
                roomImg.setRepImgYn("N");
            }
            roomImgService.saveRoomImg(roomImg,roomImgFilsList.get(i));

        }

        return room.getId();
    }

    public RoomFormDto getRoomDtl(Long roomId){
        List<RoomImg> roomImgList =roomImgRepository.findRoomImgsByRoomReservationIdAsc(roomId);

        List<RoomImgDto> roomImgDtoList =new ArrayList<>();

        for (RoomImg roomImg : roomImgList) {
            RoomImgDto roomImgDto = RoomImgDto.of(roomImg);
            roomImgDtoList.add(roomImgDto);
        }
        Room room = roomRepository.findOne(roomId);
        // Room -> RoomFormDto modelMapper
        RoomFormDto roomFormDto = RoomFormDto.of(room);
        roomFormDto.setRoomImgDtoList(roomImgDtoList);
        return roomFormDto;
    }

    @Transactional
    public Long updateRoom(RoomFormDto roomFormDto,List<MultipartFile> roomImgFileList) throws Exception{
        // 객실 변경
        Room room =roomRepository.findOne(roomFormDto.getId());
        room.updateRoom(roomFormDto);
        // 객실 이미지 변경
        List<Long> roomImgIds = roomFormDto.getRoomImgIds();

        for (int i =0; i<roomImgFileList.size(); i++){
            roomImgService.updateRoomImg(roomImgIds.get(i),roomImgFileList.get(i));
        }
        return room.getId();
    }
    @Transactional(readOnly = true)
    public Page<Room> getAdminRoomPage(ReservationDto reservationDto, Pageable pageable){
        return roomRepositoryCustom.getAdminRoomPage(reservationDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainRoomDto> getMainRoomPages(ReservationDto reservationDto, Pageable pageable){
        return roomRepositoryCustom.getMainRoomPage(reservationDto,pageable);
    }

    public Room findOne(Long id){
        return roomRepository.findOne(id);
    }
    public List<Room> findAll(){
        return roomRepository.findAll();
    }

}
