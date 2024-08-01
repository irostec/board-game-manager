package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameIntegration;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameIntegrationRepository extends JpaRepository<BoardGameIntegration, Long> {

    Stream<BoardGameIntegration> findByIntegratedAndIntegratingIn(BoardGame integrated, Collection<BoardGame> integrating);

}
