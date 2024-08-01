package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGamePublisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGamePublisherRepository extends JpaRepository<BoardGamePublisher, Long> {

    Stream<BoardGamePublisher> findByBoardGameAndPublisherIn(BoardGame boardGame, Collection<Publisher> publishers);

}
