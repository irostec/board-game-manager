package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameImplementation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameImplementationRepository  extends JpaRepository<BoardGameImplementation, Long> {
}
