package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "board_game_inclusion")
@Getter
@Setter
public class BoardGameInclusion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String reason;

    @OneToMany(mappedBy="boardGameInclusion")
    private Set<BoardGameInclusionDetail> boardGameInclusionDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameInclusion that = (BoardGameInclusion) o;
        return id == that.id && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reason);
    }

    @Override
    public String toString() {
        return "BoardGameInclusion{" +
                "id=" + id +
                ", reason='" + reason + '\'' +
                '}';
    }

}
