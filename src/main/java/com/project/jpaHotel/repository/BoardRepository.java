package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Board;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.dto.BoardSearchDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class BoardRepository extends BoardRepositoryCustomImpl{
    @PersistenceContext
    private final EntityManager em;

    public BoardRepository(EntityManager em) {
        super(em);
        this.em = em;
    }
    @Transactional
    public void save(Board board){
        em.persist(board);
    }
    public Board findOne(Long id){
        return em.find(Board.class,id);
    }
    public List<Board> findAll(){
        return em.createQuery("SELECT b FROM Board b",Board.class)
                .getResultList();
    }
    public int deleteById(Long id){
        return em.createQuery("DELETE FROM Board b WHERE b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }


}
