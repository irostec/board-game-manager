package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    Stream<Family> findByDataSourceAndExternalIdIn(DataSource dataSource, Collection<String> externalIds);

}
