package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "board_game_fixed_data")
@Data
public class BoardGameFixedData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long boardGameId;

    @Column(nullable = false)
    private short yearOfPublication;

    @Column(nullable = false)
    private short minimumPlayers;

    @Column(nullable = false)
    private short maximumPlayers;

    @Column(nullable = false)
    private short averagePlaytimeInMinutes;

    @Column(nullable = false)
    private short minimumPlaytimeInMinutes;

    @Column(nullable = false)
    private short maximumPlaytimeInMinutes;

    @Column(nullable = false)
    private short minimumAge;

}
