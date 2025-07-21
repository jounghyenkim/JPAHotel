package com.project.jpaHotel.service;

import com.project.jpaHotel.domain.Board;
import com.project.jpaHotel.domain.Member;
import com.project.jpaHotel.dto.BoardSearchDto;
import com.project.jpaHotel.dto.WriteFormDto;
import com.project.jpaHotel.repository.BoardRepository;
import com.project.jpaHotel.repository.BoardRepositoryCustomImpl;
import com.project.jpaHotel.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public Board writeBoard(WriteFormDto writeFormDto, Principal principal , HttpSession httpSession) {

        String email= memberService.loadMemberEmail(principal,httpSession);
        String name = memberService.loadMemberName(principal,httpSession);


        List<Member> members = memberRepository.findByEmail(email);
        Member member = members.get(0);


        Board board =  Board.writeBoard(writeFormDto,member,name);
        boardRepository.save(board);
        return board;

    }
    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    public Board getId(Long boardId) {
        return boardRepository.findOne(boardId);
    }

    public void BoardAs(WriteFormDto writeFormDto, Long id) {
        Board board = boardRepository.findOne(id);
        board =board.boardAs(writeFormDto);
        boardRepository.save(board);
    }
    @Transactional
    public void deleteById(Long id) {
        int deletedCount = boardRepository.deleteById(id);

        if (deletedCount == 0) {
            throw new EntityNotFoundException("삭제할 게시글이 없습니다. ID: " + id);
        }
    }
    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage(boardSearchDto,pageable);
    }
}
