package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameReferenceRepository extends JpaRepository<BoardGameReference, Long> {

    Stream<BoardGameReference> findByDataSourceAndExternalIdIn(DataSource dataSource, Collection<String> externalIds);

}
