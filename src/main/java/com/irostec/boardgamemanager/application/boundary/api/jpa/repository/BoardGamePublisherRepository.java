package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGamePublisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGamePublisherRepository extends JpaRepository<BoardGamePublisher, Long> {

    Stream<BoardGamePublisher> findByBoardGameIdAndPublisherIdIn(Long boardGameId, Collection<Long> publisherIds);

}
