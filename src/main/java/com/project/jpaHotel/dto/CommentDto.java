package com.project.jpaHotel.dto;

import com.project.jpaHotel.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CommentDto {
    private Long id;

    private Long boardId;
    private Long parentId;
    private String memberName;

    private String content;

    private LocalDateTime regTime;
    private String upTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public Comment createComment() {
        return modelMapper.map(this, Comment.class);
    }
    public static CommentDto of(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);

        if (comment.getRegTime() != null) {
            commentDto.setUpTime(comment.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            commentDto.setUpTime("등록시간 없음");
        }

        if (comment.getParent() != null) {
            commentDto.setParentId(comment.getParent().getId());
        }

        return commentDto;
    }

    public void timeSetting() {
        this.upTime = getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
