package com.probono.board.controller;

import com.probono.board.dto.BoardCreateRequest;
import com.probono.board.entity.Board;
import com.probono.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {
    @Autowired
    private BoardService boardservice;

    // @GetMapping("/")
    // public String Helloword(){
    //     return "Hello World";
    // }
    @PostMapping("/qa-board/write")
    public String boardWriteDo(@RequestBody BoardCreateRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Board board = new Board();
        board.setUsername(username);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        boardservice.write(board);
        return "";
    }
    @GetMapping("/qa-board/getmyboard")
    public List<Board> boardList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return boardservice.getmyboard(username);
    }
    @PostMapping("/qa-board/delete")
    public String boardDelete(@RequestParam("id") Integer id){

        boardservice.delete(id);
        return "";
    }
    @PostMapping("/qa-board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, @RequestBody Board board){
        board.setId(id);
        boardservice.write(board);
        return "";
    }
}