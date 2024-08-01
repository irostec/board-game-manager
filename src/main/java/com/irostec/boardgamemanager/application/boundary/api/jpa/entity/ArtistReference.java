package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "artist_reference")
@Getter
@Setter
public class ArtistReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="artist_id", nullable=false)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="data_source_id", nullable=false)
    private DataSource dataSource;

    @Column(nullable = false)
    private String externalId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistReference that = (ArtistReference) o;
        return id == that.id && Objects.equals(artist, that.artist) && Objects.equals(externalId, that.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artist, externalId);
    }

    @Override
    public String toString() {
        return "ArtistReference{" +
                "id=" + id +
                ", artist=" + artist +
                ", externalId='" + externalId + '\'' +
                '}';
    }

}
