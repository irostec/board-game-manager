package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

    Optional<DataSource> findByName(DataSourceName name);

}
