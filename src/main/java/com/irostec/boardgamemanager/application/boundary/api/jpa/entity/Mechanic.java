package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "mechanic")
@Getter
@Setter
public class Mechanic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="data_source_id", nullable=false)
    private DataSource dataSource;

    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="mechanic")
    private Set<BoardGameMechanic> boardGameMechanics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mechanic mechanic = (Mechanic) o;
        return id == mechanic.id && Objects.equals(externalId, mechanic.externalId) && Objects.equals(name, mechanic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, name);
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
