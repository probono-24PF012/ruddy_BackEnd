package com.probono.board.controller;

import com.probono.board.entity.Board;
import com.probono.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {
    @Autowired
    private BoardService boardservice;

    @PostMapping("/board/write")
    public String boardWriteDo(Board board){
        boardservice.write(board);
        return "";
    }
    @GetMapping("/board/getmyboard")
    public List<Board> boardList(@RequestParam("username") String username){

        return boardservice.getmyboard(username);
    }
    @PostMapping("/board/delete")
    public String boardDelete(@RequestParam("id") Integer id){

        boardservice.delete(id);
        return "";
    }
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, @RequestBody Board board){
        board.setId(id);
        boardservice.write(board);
        return "";
    }
}