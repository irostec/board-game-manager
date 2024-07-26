package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameMechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameMechanicRepository extends JpaRepository<BoardGameMechanic, Long> {

    Stream<BoardGameMechanic> findByBoardGameIdAndMechanicIdIn(Long boardGameId, Collection<Long> mechanicIds);

}
