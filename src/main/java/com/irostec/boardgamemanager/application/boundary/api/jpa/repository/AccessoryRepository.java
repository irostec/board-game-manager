package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Accessory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.AccessoryReference;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface AccessoryRepository extends JpaRepository<Accessory, Long>  {

    @Query(
            "SELECT new org.apache.commons.lang3.tuple.ImmutablePair(accessory, accessoryReference) " +
            "FROM Accessory accessory " +
            "LEFT JOIN AccessoryReference accessoryReference ON accessory.id = accessoryReference.accessoryId " +
            "WHERE accessoryReference.dataSourceId = :dataSourceId AND accessoryReference.externalId IN :externalIds"
    )
    Stream<ImmutablePair<Accessory, AccessoryReference>> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}
