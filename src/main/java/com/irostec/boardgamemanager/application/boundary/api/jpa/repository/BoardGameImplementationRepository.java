package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameImplementation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface BoardGameImplementationRepository  extends JpaRepository<BoardGameImplementation, Long> {

    Stream<BoardGameImplementation> findByImplementedAndImplementingIn(BoardGame implemented, Collection<BoardGame> implementers);

}
