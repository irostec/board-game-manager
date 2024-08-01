package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "board_game_inclusion_detail")
@Getter
@Setter
public class BoardGameInclusionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_game_inclusion_id", nullable=false)
    private BoardGameInclusion boardGameInclusion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_game_user_id", nullable=false)
    private BoardGameUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_game_id", nullable=false)
    private BoardGame boardGame;

    private BigDecimal rating;

    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameInclusionDetail that = (BoardGameInclusionDetail) o;
        return id == that.id && Objects.equals(rating, that.rating) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment);
    }

    @Override
    public String toString() {
        return "BoardGameInclusionDetail{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }

}
