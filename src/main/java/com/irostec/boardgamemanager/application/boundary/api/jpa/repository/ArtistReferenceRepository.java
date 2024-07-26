package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ArtistReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface ArtistReferenceRepository extends JpaRepository<ArtistReference, Long> {

    Stream<ArtistReference> findByDataSourceIdAndExternalIdIn(Long dataSourceId, Collection<String> externalIds);

}
