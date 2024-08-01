package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "board_game_fixed_data")
@Getter
@Setter
public class BoardGameFixedData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

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

    @OneToOne(mappedBy = "fixedData", fetch = FetchType.LAZY)
    private BoardGame boardGame;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGameFixedData that = (BoardGameFixedData) o;
        return id == that.id && yearOfPublication == that.yearOfPublication && minimumPlayers == that.minimumPlayers && maximumPlayers == that.maximumPlayers && averagePlaytimeInMinutes == that.averagePlaytimeInMinutes && minimumPlaytimeInMinutes == that.minimumPlaytimeInMinutes && maximumPlaytimeInMinutes == that.maximumPlaytimeInMinutes && minimumAge == that.minimumAge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, yearOfPublication, minimumPlayers, maximumPlayers, averagePlaytimeInMinutes, minimumPlaytimeInMinutes, maximumPlaytimeInMinutes, minimumAge);
    }

    @Override
    public String toString() {
        return "BoardGameFixedData{" +
                "id=" + id +
                ", yearOfPublication=" + yearOfPublication +
                ", minimumPlayers=" + minimumPlayers +
                ", maximumPlayers=" + maximumPlayers +
                ", averagePlaytimeInMinutes=" + averagePlaytimeInMinutes +
                ", minimumPlaytimeInMinutes=" + minimumPlaytimeInMinutes +
                ", maximumPlaytimeInMinutes=" + maximumPlaytimeInMinutes +
                ", minimumAge=" + minimumAge +
                '}';
    }

}
