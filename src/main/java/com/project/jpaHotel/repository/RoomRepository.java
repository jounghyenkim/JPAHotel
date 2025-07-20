package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Room;
import com.project.jpaHotel.dto.ReservationDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;

    public void save(Room room){
        em.persist(room);
    }
    public Room findOne(Long id){
       return em.find(Room.class,id);
    }
    public List<Room> findAll(){
        return em.createQuery("select r from Room r",Room.class)
                .getResultList();
    }



}
