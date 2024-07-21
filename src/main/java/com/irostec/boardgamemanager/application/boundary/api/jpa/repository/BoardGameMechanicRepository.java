package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameMechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameMechanicRepository extends JpaRepository<BoardGameMechanic, Long> {
}
