package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "data_source")
@Getter
@Setter
public class DataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSourceName name;

    @OneToMany(mappedBy="dataSource")
    private Set<AccessoryReference> accessoryReferences;

    @OneToMany(mappedBy="dataSource")
    private Set<Category> categories;

    @OneToMany(mappedBy="dataSource")
    private Set<Family> families;

    @OneToMany(mappedBy="dataSource")
    private Set<ArtistReference> artistReferences;

    @OneToMany(mappedBy="dataSource")
    private Set<DesignerReference> designerReferences;

    @OneToMany(mappedBy="dataSource")
    private Set<PublisherReference> publisherReferences;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSource that = (DataSource) o;
        return id == that.id && name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
