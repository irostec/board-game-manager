package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository  extends JpaRepository<Artist, Long> {
}
