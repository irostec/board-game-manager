package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameAccessory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameAccessoryRepository extends JpaRepository<BoardGameAccessory, Long> {
}
