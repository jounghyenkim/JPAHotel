package com.project.jpaHotel.dto;


import com.project.jpaHotel.domain.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class WriteFormDto {
    private Long id;
    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력 값 입니다.")
    private String content;

    private String writer;

    private LocalDate localDate;


    private static ModelMapper modelMapper = new ModelMapper();

    public static WriteFormDto of(Board board) {
        return modelMapper.map(board, WriteFormDto.class);
    }
}
