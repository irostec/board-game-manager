package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.ImageTypeName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "image_type")
@Getter
@Setter
public class ImageType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageTypeName name;

    @OneToMany(mappedBy="imageType")
    private Set<Image> images;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageType imageType = (ImageType) o;
        return id == imageType.id && name == imageType.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "ImageType{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
