package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "family")
@Getter
@Setter
public class Family {

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

    @OneToMany(mappedBy="family")
    private Set<BoardGameFamily> boardGameFamilies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Family family = (Family) o;
        return id == family.id && Objects.equals(externalId, family.externalId) && Objects.equals(name, family.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, name);
    }

    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
