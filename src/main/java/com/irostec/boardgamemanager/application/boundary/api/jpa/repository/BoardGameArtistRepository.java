package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Artist;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameArtistRepository extends JpaRepository<BoardGameArtist, Long> {

    Stream<BoardGameArtist> findByBoardGameAndArtistIn(BoardGame boardGame, Collection<Artist> artists);

}
