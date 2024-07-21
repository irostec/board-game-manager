package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.AccessoryReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface AccessoryReferenceRepository  extends JpaRepository<AccessoryReference, Long> {

    Stream<AccessoryReference> findByDataSourceIdAndExternalIdIn(Long dataSourceId, Collection<String> externalIds);

}
