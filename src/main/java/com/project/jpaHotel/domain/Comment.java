package com.project.jpaHotel.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private List<Comment> comments =new ArrayList<>();
    public Comment(){};

    public Comment (Board board, Member member, String content, Comment parent) {
        this.board = board;
        this.member = member;
        this.content = content;
        this.parent = parent;
    }

    public Long updateComment (String content) {
        this.content = content;
        return this.board.getId();
    }
}
