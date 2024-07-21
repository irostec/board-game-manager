package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    @Query(
        "SELECT new org.apache.commons.lang3.tuple.ImmutablePair(boardGame, boardGameReference) " +
        "FROM BoardGame boardGame " +
        "LEFT JOIN BoardGameReference boardGameReference ON boardGame.id = boardGameReference.boardGameId " +
        "WHERE boardGameReference.dataSourceId = :dataSourceId AND boardGameReference.externalId IN :externalIds"
    )
    Stream<ImmutablePair<BoardGame, BoardGameReference>> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

    @Query(
        "SELECT new org.apache.commons.lang3.tuple.ImmutablePair(boardGame, boardGameReference) " +
        "FROM BoardGame boardGame " +
        "LEFT JOIN BoardGameReference boardGameReference ON boardGame.id = boardGameReference.boardGameId " +
        "WHERE boardGameReference.dataSourceId = :dataSourceId AND boardGameReference.externalId = :externalId"
    )
    Optional<ImmutablePair<BoardGame, BoardGameReference>> findByDataSourceIdAndExternalId(Long dataSourceId, String externalId);

}
