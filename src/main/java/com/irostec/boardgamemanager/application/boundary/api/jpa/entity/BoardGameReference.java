package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "board_game_reference")
@Data
public class BoardGameReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long boardGameId;

    @Column(nullable = false)
    private long dataSourceId;

    @Column(nullable = false)
    private String externalId;

    private BigDecimal averageRating;

}
