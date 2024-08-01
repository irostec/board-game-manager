package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "board_game_implementation")
@Getter
@Setter
public class BoardGameImplementation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="implemented_board_game_id", nullable=false)
    private BoardGame implemented;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="implementer_board_game_id", nullable=false)
    private BoardGame implementing;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameImplementation that = (BoardGameImplementation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BoardGameImplementation{" +
                "id=" + id +
                '}';
    }

}
