package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface FamilyRepository  extends JpaRepository<Family, Long> {

    Stream<Family> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}