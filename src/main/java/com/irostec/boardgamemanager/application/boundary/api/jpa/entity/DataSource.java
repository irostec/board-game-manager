package com.irostec.boardgamemanager.application.boundary.api.jpa.entity;

import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "data_source")
@Data
public class DataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSourceName name;

}
