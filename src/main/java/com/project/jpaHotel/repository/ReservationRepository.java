package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Reservation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final EntityManager em;

    public void save(Reservation reservation){
        em.persist(reservation);
    }

    public Reservation findOne(Long id){
        return em.find(Reservation.class,id);
    }
    public List<Reservation> findAll(){
        return em.createQuery("select r from Reservation r",Reservation.class)
                .getResultList();
    }
    public List<Reservation> findByMemberId(Long memberId){
        return em.createQuery("SELECT r FROM Reservation r WHERE r.member.id = :memberId",Reservation.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }
    public void deleteByReservationId(Long id){
        em.createQuery("DELETE FROM Reservation r WHERE r.id = :id")
                .setParameter("id",id)
                .executeUpdate();
    }
    public void deleteAll(){
        em.createQuery("DELETE FROM Reservation r")
                .executeUpdate();
    }
}
