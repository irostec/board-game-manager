package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "board_game_user")
@Getter
@Setter
public class BoardGameUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy="user")
    private Set<BoardGameInclusionDetail> inclusionDetails;

    @OneToMany(mappedBy="user")
    private Set<BoardGameVariableData> variableData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameUser that = (BoardGameUser) o;
        return id == that.id && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "BoardGameUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

}
