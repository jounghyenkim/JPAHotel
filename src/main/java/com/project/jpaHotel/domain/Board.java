package com.project.jpaHotel.domain;

import com.project.jpaHotel.dto.WriteFormDto;
import com.project.jpaHotel.service.MemberService;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String content;
    private String writer;
    private LocalDate localDate;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public static Board writeBoard(WriteFormDto boardWriteFormDto, Member member, String name){
        Board board=new Board();;
        board.setTitle(boardWriteFormDto.getTitle());
        board.setContent(boardWriteFormDto.getContent());
        board.setLocalDate(LocalDate.now());
        board.setWriter(name);
        board.setMember(member);
        return board;
    }

    public Board boardAs(WriteFormDto writeFormDto) {
        this.title = writeFormDto.getTitle();
        this.content = writeFormDto.getContent();
        return this;
    }
}
