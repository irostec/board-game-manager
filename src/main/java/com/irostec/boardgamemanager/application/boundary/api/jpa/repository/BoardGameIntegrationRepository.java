package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameIntegration;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameIntegrationRepository extends JpaRepository<BoardGameIntegration, Long> {

    Stream<BoardGameIntegration> findByIntegratedBoardGameIdAndIntegratingBoardGameIdIn(Long integratedBoardGameId, Collection<Long> integratingBoardGameIds);

}
