package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameMechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameMechanicRepository extends JpaRepository<BoardGameMechanic, Long> {

    Stream<BoardGameMechanic> findByBoardGameAndMechanicIn(BoardGame boardGame, Collection<Mechanic> mechanics);

}
