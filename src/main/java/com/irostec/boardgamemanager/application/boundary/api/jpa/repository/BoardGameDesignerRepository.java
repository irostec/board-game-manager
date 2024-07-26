package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameDesigner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameDesignerRepository extends JpaRepository<BoardGameDesigner, Long> {

    Stream<BoardGameDesigner> findByBoardGameIdAndDesignerIdIn(Long boardGameId, Collection<Long> designerIds);

}
