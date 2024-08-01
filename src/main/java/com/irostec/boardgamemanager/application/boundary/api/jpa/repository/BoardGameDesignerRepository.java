package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameDesigner;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameDesignerRepository extends JpaRepository<BoardGameDesigner, Long> {

    Stream<BoardGameDesigner> findByBoardGameAndDesignerIn(BoardGame boardGame, Collection<Designer> designers);

}
