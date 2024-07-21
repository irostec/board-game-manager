package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.ImageTypeName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "image_type")
@Data
public class ImageType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageTypeName name;

}
