package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "board_game")
@Getter
@Setter
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_data_id", referencedColumnName = "id")
    private BoardGameFixedData fixedData;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameReference> references;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameAccessory> accessories;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameArtist> artists;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameCategory> categories;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameDesigner> designers;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameFamily> families;

    @OneToMany(mappedBy="expanded")
    private Set<BoardGameExpansion> expanded;

    @OneToMany(mappedBy="expander")
    private Set<BoardGameExpansion> expanding;

    @OneToMany(mappedBy="implemented")
    private Set<BoardGameImplementation> implemented;

    @OneToMany(mappedBy="implementing")
    private Set<BoardGameImplementation> implementing;

    @OneToMany(mappedBy="integrated")
    private Set<BoardGameIntegration> integrated;

    @OneToMany(mappedBy="integrating")
    private Set<BoardGameIntegration> integrating;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameInclusionDetail> inclusionDetails;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameMechanic> mechanics;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGamePublisher> publishers;

    @OneToMany(mappedBy="boardGame")
    private Set<BoardGameVariableData> variableData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGame boardGame = (BoardGame) o;
        return id == boardGame.id && Objects.equals(name, boardGame.name) && Objects.equals(description, boardGame.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "BoardGame{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
