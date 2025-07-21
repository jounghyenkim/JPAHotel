package com.project.jpaHotel.controller;

import com.project.jpaHotel.domain.Board;
import com.project.jpaHotel.dto.BoardSearchDto;
import com.project.jpaHotel.dto.CommentDto;
import com.project.jpaHotel.dto.WriteFormDto;
import com.project.jpaHotel.repository.BoardRepository;
import com.project.jpaHotel.service.BoardService;
import com.project.jpaHotel.service.CommentService;
import com.project.jpaHotel.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final HttpSession httpSession;
    private final BoardRepository boardRepository;


    @GetMapping(value = {"/boardLists","/boardLists/{page}"})
    public String boardList(Model model, @PathVariable("page") Optional<Integer> page, Principal principal, BoardSearchDto boardSearchDto) {
        //Page<Board> boardList = boardService.getBoardList(page);
        //model.addAttribute("boardList", boardList);
        String name = memberService.loadMemberName(principal, httpSession);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 41);
        Page<Board> boards = boardService.getBoardPage(boardSearchDto, pageable);
        model.addAttribute("name", name);
        model.addAttribute("maxPage", 5);
        model.addAttribute("boards",boards);
        model.addAttribute("boardSearchDto",new BoardSearchDto());
        return "/faq/board";
    }

    @GetMapping(value = "/write")
    public String writeForm(Model model, WriteFormDto writeFormDto, Principal principal) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        model.addAttribute("writeFormDto", writeFormDto);
        return "/faq/writeBoardForm";
    }



    @GetMapping(value = "/detail/{id}")
    public String userBoardDetail(Model model, @PathVariable("id") Long boardId, Principal principal) {
        try {
            String name = memberService.loadMemberName(principal, httpSession);
            model.addAttribute("name", name);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/faq/boardLists";
        }

        WriteFormDto writeFormDto;
        try {
            writeFormDto = WriteFormDto.of(boardService.getId(boardId));
            model.addAttribute("writeFormDto", writeFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/faq/boardLists";
        }

        try {
            List<CommentDto> mainCommentList = commentService.mainCommentList(boardId);
            List<CommentDto> subCommentList = commentService.subCommentList(boardId);

            model.addAttribute("mainCommentList", mainCommentList);
            model.addAttribute("subCommentList", subCommentList);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/faq/boardLists";
        }

        return "/faq/userBoardDetail";
    }


    @GetMapping(value = "/boardDetail/{id}")
    public String boardDetail(Model model, @PathVariable("id") Long boardId, Principal principal) {
        WriteFormDto writeFormDto = WriteFormDto.of(boardService.getId(boardId));
        String name = memberService.loadMemberName(principal, httpSession);
        System.out.println(writeFormDto);
        model.addAttribute("writeFormDto", writeFormDto);
        model.addAttribute("name", name);
        return "/faq/boardDetail";
    }
    @PostMapping(value = "/write")
    public String write(@Valid WriteFormDto writeFormDto, BindingResult bindingResult,
                        Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            String name = memberService.loadMemberName(principal, httpSession);
            model.addAttribute("name", name);
            return "/faq/writeBoardForm";
        }
        Board board = boardService.writeBoard(writeFormDto, principal, httpSession);
        boardRepository.save(board);
        return "redirect:/faq/boardLists";
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid WriteFormDto writeFormDto, BindingResult bindingResult, @PathVariable("id") Long boardId,
                         Model model, Principal principal) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        if (bindingResult.hasErrors()) {
            return "/faq/boardDetail";
        }
        boardService.BoardAs(writeFormDto, boardId);
        return "redirect:/faq/boardLists";
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Long boardId, Model model) {
        boardService.deleteById(boardId);
        model.addAttribute("id", boardId);
        return "redirect:/faq/boardLists";
    }
}
