package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "board_game_inclusion_detail")
@Data
public class BoardGameInclusionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long boardGameInclusionId;

    @Column(nullable = false)
    private long boardGameUserId;

    @Column(nullable = false)
    private long boardGameId;

}
