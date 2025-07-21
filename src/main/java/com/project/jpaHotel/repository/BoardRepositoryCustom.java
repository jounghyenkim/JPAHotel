package com.project.jpaHotel.repository;

import com.project.jpaHotel.domain.Board;
import com.project.jpaHotel.dto.BoardSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

}
