package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.RoomImg;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomImgRepository {
    private final EntityManager em;

    public void save(RoomImg roomImg){
        em.persist(roomImg);
    }
    public RoomImg findOne(Long id){
       return em.find(RoomImg.class,id);
    }
    public List<RoomImg> findRoomImgsByRoomReservationIdAsc(Long roomId) {
        String jpql = "SELECT r FROM RoomImg r WHERE r.room.id = :roomId ORDER BY r.id ASC";
        return em.createQuery(jpql, RoomImg.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }
    public RoomImg findByRoomIdAndRepImgYn(Long roomId, String repImgYn) {
        String jpql = "SELECT r FROM RoomImg r WHERE r.room.id = :roomId AND r.repImgYn = :repImgYn";

        List<RoomImg> resultList = em.createQuery(jpql, RoomImg.class)
                .setParameter("roomId", roomId)
                .setParameter("repImgYn", repImgYn)
                .getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
