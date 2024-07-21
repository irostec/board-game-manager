package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFamily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameFamilyRepository extends JpaRepository<BoardGameFamily, Long> {
}
