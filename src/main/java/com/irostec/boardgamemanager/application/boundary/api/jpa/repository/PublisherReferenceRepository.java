package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.PublisherReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface PublisherReferenceRepository extends JpaRepository<PublisherReference, Long> {

    Stream<PublisherReference> findByDataSourceIdAndExternalIdIn(Long dataSourceId, Collection<String> externalIds);

}
