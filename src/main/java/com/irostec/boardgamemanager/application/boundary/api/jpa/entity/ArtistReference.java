package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "artist_reference")
@Data
public class ArtistReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long dataSourceId;

    @Column(nullable = false)
    private long artistId;

    @Column(nullable = false)
    private String externalId;

}
