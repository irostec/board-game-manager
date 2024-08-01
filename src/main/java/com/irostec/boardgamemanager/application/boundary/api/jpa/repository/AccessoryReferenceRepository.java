package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.AccessoryReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface AccessoryReferenceRepository  extends JpaRepository<AccessoryReference, Long> {

    Stream<AccessoryReference> findByDataSourceAndExternalIdIn(DataSource dataSource, Collection<String> externalIds);

}
