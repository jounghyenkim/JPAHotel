package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.RoomImg;
import com.project.jpaHotel.repository.RoomImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomImgService {
    @Value("${roomImgLocation}") //application.properties에 roomImgLocation
    private String roomImgLocation;
    private final RoomImgRepository roomImgRepository;
    private final FileService fileService;


    public void saveRoomImg(RoomImg roomImg, MultipartFile roomImgFile) throws  Exception{
        String oriImgName = roomImgFile.getOriginalFilename();
        String imaName= "";
        String imgUrl ="";
        System.out.println(oriImgName);
        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){// oriImgName 문자열로 비어있지 않으면 실행
            imaName =fileService.uploadFile(roomImgLocation,oriImgName,
                    roomImgFile.getBytes());
            System.out.println("imaName = " + imaName);

        }
        // 객실 이미지 정보 저장
        // oriImgName : 객실 이미지 파일의 원래 이름
        // imgName : 실제 로컬에 저장된 상품의 이미지 파일의 이름
        // imgUrl : 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        roomImg.updateRoomImg(oriImgName,imaName,imgUrl);
        roomImgRepository.save(roomImg);
    }

    public void updateRoomImg(Long roomImgId, MultipartFile roomImgFile) throws  Exception{
        if (!roomImgFile.isEmpty()){ // 객실 이미지를 수정한 경우 객실 이미지 업데이트
            RoomImg savedRoomImg = roomImgRepository.findOne(roomImgId); // 기존 엔티티 조회
            // 기존에 등록된 객실 이미지 파일이 있는경우 파일 삭제
            if (StringUtils.isEmpty(savedRoomImg.getImgName())){
                fileService.deleteFile(roomImgLocation+"/" +savedRoomImg.getImgName());
            }
            String oriImgName = roomImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(roomImgLocation,oriImgName,
                    roomImgFile.getBytes()); // 파일 업로드
            String imgUrl = "/images/room" +imgName;
            // 변경된 객실 이미지 정보를 세팅
            // 객실 등록을 하는 경우에는 RoomImgRepository.save() 로직을 호출 하지만
            // 호출하지 않았습니다.
            // savedRoomImg 엔티티는 현재 영속성 상태이다.
            // 그래서 데이터를 변경하는 것만으로 변경 감지 기능이 동작
            // 트랜잭션이 끝날때 update 쿼리가 실행 된다.
            // 영속성 상태여야 사용가능
            savedRoomImg.updateRoomImg(oriImgName,imgName,imgUrl);
        }

    }
}
