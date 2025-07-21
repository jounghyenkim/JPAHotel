package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Board;
import com.project.jpaHotel.domain.Comment;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.dto.CommentDto;
import com.project.jpaHotel.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BoardService boardService;
    public List<CommentDto> mainCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findCommentsWithNullParentByBoardId(boardId);
        List<CommentDto> mainCommentList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = CommentDto.of(comment);
            mainCommentList.add(commentDto);
        }
        return mainCommentList;
    }

    public List<CommentDto> subCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findCommentsWithNotNullParentByBoardId(boardId);
        List<CommentDto> subCommentList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = CommentDto.of(comment);
            subCommentList.add(commentDto);
        }
        return subCommentList;
    }

    public Comment commentWrite(HttpSession httpSession, Principal principal, CommentDto commentDto) {
        Member member = memberService.findMember(httpSession, principal);


        Board board = boardService.getId(commentDto.getBoardId());
        Comment parent = null;
        if (commentDto.getParentId() != null) {
            parent = commentRepository.findOne(commentDto.getParentId());
        }
        Comment comment = new Comment(board, member, commentDto.getContent(), parent);
        commentRepository.save(comment);
        return comment;
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    public Long commentDelete(Comment comment) {
        Long boardId = comment.getBoard().getId();
        commentRepository.deleteCommentById(comment.getId());
        return boardId;
    }
    public Long commentUpdate(Comment comment, String content) {
        return comment.updateComment(content);
    }
}
