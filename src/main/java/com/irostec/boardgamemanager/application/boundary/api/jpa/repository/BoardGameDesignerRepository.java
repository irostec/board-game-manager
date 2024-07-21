package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameDesigner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameDesignerRepository extends JpaRepository<BoardGameDesigner, Long> {
}
