package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Cart;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartRepository {
    private final EntityManager em;

    public void save(Cart cart){
        em.persist(cart);
    }
    public List<Cart> findByMemberId(Long memberId) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.member.id = :memberId", Cart.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

}
