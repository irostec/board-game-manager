package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameIntegration;

public interface BoardGameIntegrationRepository extends JpaRepository<BoardGameIntegration, Long> {
}
