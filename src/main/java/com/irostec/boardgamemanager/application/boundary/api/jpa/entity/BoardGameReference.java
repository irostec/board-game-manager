package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "board_game_reference")
@Getter
@Setter
public class BoardGameReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_game_id", nullable=false)
    private BoardGame boardGame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="data_source_id", nullable=false)
    private DataSource dataSource;

    @Column(nullable = false)
    private String externalId;

    private BigDecimal averageRating;

    @OneToMany(mappedBy="boardGameReference")
    private Set<Image> images;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameReference that = (BoardGameReference) o;
        return id == that.id && Objects.equals(externalId, that.externalId) && Objects.equals(averageRating, that.averageRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, averageRating);
    }

    @Override
    public String toString() {
        return "BoardGameReference{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }

}
