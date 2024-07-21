package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFixedData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameFixedDataRepository extends JpaRepository<BoardGameFixedData, Long> {
}
