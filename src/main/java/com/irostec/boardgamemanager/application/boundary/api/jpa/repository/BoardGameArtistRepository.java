package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameArtistRepository extends JpaRepository<BoardGameArtist, Long> {
}
