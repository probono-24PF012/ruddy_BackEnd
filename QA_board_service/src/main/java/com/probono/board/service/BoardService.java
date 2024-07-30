package com.probono.board.service;

import com.probono.board.entity.Board;
import com.probono.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    public void write(Board board){
        boardRepository.save(board);
    }
    public List<Board> getmyboard(String username){
        return boardRepository.findByUsername(username);
    }

    public void delete(Integer id){
        boardRepository.deleteById(id);
    }
}
