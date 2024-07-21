package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameExpansion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameExpansionRepository extends JpaRepository<BoardGameExpansion, Long> {
}
