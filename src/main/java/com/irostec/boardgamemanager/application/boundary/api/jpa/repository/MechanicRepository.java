package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface MechanicRepository extends JpaRepository<Mechanic, Long>  {

    Stream<Mechanic> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}
