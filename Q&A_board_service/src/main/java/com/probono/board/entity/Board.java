package com.probono.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Test")
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private String username;
}

