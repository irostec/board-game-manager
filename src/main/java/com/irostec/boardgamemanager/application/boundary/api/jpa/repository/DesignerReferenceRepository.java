package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DesignerReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface DesignerReferenceRepository extends JpaRepository<DesignerReference, Long> {

    Stream<DesignerReference> findByDataSourceIdAndExternalIdIn(Long dataSourceId, Collection<String> externalIds);

}
