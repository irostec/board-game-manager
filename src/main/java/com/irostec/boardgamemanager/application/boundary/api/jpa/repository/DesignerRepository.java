package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Designer;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DesignerReference;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface DesignerRepository  extends JpaRepository<Designer, Long> {

    @Query(
            "SELECT new org.apache.commons.lang3.tuple.ImmutablePair(designer, designerReference) " +
            "FROM Designer designer " +
            "LEFT JOIN DesignerReference designerReference ON designer.id = designerReference.designerId " +
            "WHERE designerReference.dataSourceId = :dataSourceId AND designerReference.externalId IN :externalIds"
    )
    Stream<ImmutablePair<Designer, DesignerReference>> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}
