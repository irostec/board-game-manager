package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameExpansion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameExpansionRepository extends JpaRepository<BoardGameExpansion, Long> {

    Stream<BoardGameExpansion> findByExpandedAndExpanderIn(
        BoardGame expanded,
        Collection<BoardGame> expanders
    );

}
