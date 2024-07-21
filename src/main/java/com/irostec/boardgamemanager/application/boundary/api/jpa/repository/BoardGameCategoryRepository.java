package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameCategoryRepository extends JpaRepository<BoardGameCategory, Long> {
}
