package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGamePublisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGamePublisherRepository extends JpaRepository<BoardGamePublisher, Long> {
}
