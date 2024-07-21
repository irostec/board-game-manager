package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "board_game_rating")
@Data
public class BoardGameRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long boardGameInclusionDetailId;

    @Column(nullable = false)
    private BigDecimal rating;

}
