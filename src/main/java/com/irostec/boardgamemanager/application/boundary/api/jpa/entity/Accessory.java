package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "accessory")
@Getter
@Setter
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="accessory")
    private Set<AccessoryReference> accessoryReferences;

    @OneToMany(mappedBy="accessory")
    private Set<BoardGameAccessory> boardGameAccessories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accessory accessory = (Accessory) o;
        return id == accessory.id && Objects.equals(name, accessory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Accessory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
