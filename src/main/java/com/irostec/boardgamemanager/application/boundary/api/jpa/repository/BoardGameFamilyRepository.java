package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFamily;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameFamilyRepository extends JpaRepository<BoardGameFamily, Long> {

    Stream<BoardGameFamily> findByBoardGameAndFamilyIn(BoardGame boardGame, Collection<Family> families);

}
