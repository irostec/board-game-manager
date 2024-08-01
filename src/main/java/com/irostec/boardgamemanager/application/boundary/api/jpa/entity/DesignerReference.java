package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "designer_reference")
@Getter
@Setter
public class DesignerReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="data_source_id", nullable=false)
    private DataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="designer_id", nullable=false)
    private Designer designer;

    @Column(nullable = false)
    private String externalId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesignerReference that = (DesignerReference) o;
        return id == that.id && Objects.equals(externalId, that.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId);
    }

    @Override
    public String toString() {
        return "DesignerReference{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                '}';
    }

}
