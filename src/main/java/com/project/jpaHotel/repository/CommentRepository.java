package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public List<Comment> findCommentsWithNullParentByBoardId(Long boardId) {
        return em.createQuery(
                        "SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.parent IS NULL ORDER BY c.id ASC",
                        Comment.class
                )
                .setParameter("boardId", boardId)
                .getResultList();
    }
    public List<Comment> findCommentsWithNotNullParentByBoardId(Long boardId) {
        return em.createQuery(
                        "SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.parent IS NOT NULL ORDER BY c.id ASC",
                        Comment.class
                )
                .setParameter("boardId", boardId)
                .getResultList();
    }
    public void deleteCommentById(Long commentId) {
        em.createQuery("DELETE FROM Comment c WHERE c.id = :id")
                .setParameter("id", commentId)
                .executeUpdate();
    }
    public Comment findOne(Long id){
        return em.find(Comment.class,id);
    }
    public void save(Comment comment){
         em.persist(comment);
    }
}
