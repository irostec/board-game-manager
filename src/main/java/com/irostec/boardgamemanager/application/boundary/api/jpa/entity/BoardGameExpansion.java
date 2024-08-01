package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "board_game_expansion")
@Getter
@Setter
public class BoardGameExpansion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="expanded_board_game_id", nullable=false)
    private BoardGame expanded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="expander_board_game_id", nullable=false)
    private BoardGame expander;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameExpansion that = (BoardGameExpansion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BoardGameExpansion{" +
                "id=" + id +
                '}';
    }

}
