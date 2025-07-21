package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Cart;
import com.project.jpaHotel.domain.CartRoom;
import com.project.jpaHotel.dto.CartDetailDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartRoomRepository {
    private final EntityManager em;

    public void save(CartRoom cartRoom){
        em.persist(cartRoom);
    }
    public CartRoom findOne(Long id){
        return em.find(CartRoom.class,id);
    }
    public CartRoom findByRoomId(Long roomId) {
        try {
            return em.createQuery("SELECT cr FROM CartRoom cr WHERE cr.room.id = :roomId", CartRoom.class)
                    .setParameter("roomId", roomId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public CartRoom findByCartIdAndRoomId(Long cartId, Long roomId) {
        String jpql = "SELECT cr FROM CartRoom cr WHERE cr.cart.id = :cartId AND cr.room.id = :roomId";
        List<CartRoom> result = em.createQuery(jpql, CartRoom.class)
                .setParameter("cartId", cartId)
                .setParameter("roomId", roomId)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    public void deleteCartRoomById(Long cartRoomId) {
        String jpql = "DELETE FROM CartRoom cr WHERE cr.id = :cartRoomId";
        em.createQuery(jpql)
                .setParameter("cartRoomId", cartRoomId)
                .executeUpdate();
    }
    public List<CartRoom> findByMemberId(Long memberId) {
        return em.createQuery("SELECT cr FROM CartRoom cr WHERE cr.member.id = :memberId", CartRoom.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        String jpql = "select new com.project.jpaHotel.dto.CartDetailDto(" +
                "cr.id, r.roomNm, r.price, cr.count, rm.imgUrl) " +
                "from CartRoom cr, RoomImg rm " +
                "join cr.room r " +
                "where cr.cart.id = :cartId " +
                "and rm.room.id = cr.room.id " +
                "and rm.repImgYn = 'Y' " +
                "order by cr.regTime desc";

        return em.createQuery(jpql, CartDetailDto.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }


}
