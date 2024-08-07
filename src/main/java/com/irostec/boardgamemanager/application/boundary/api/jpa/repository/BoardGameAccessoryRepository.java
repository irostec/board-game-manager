package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Accessory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameAccessory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameAccessoryRepository extends JpaRepository<BoardGameAccessory, Long> {

    Stream<BoardGameAccessory> findByBoardGameAndAccessoryIn(BoardGame boardGame, Collection<Accessory> accessories);

}
