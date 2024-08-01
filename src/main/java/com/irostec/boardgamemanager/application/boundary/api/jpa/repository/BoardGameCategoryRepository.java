package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameCategory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameCategoryRepository extends JpaRepository<BoardGameCategory, Long> {

    Stream<BoardGameCategory> findByBoardGameAndCategoryIn(BoardGame boardGame, Collection<Category> categories);

}
