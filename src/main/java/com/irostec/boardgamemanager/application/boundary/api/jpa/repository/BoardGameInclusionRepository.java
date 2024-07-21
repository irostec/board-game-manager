package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameInclusionRepository extends JpaRepository<BoardGameInclusion, Long> {
}
