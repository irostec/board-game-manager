package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardGameInclusionDetailRepository extends JpaRepository<BoardGameInclusionDetail, Long> {

    Optional<BoardGameInclusionDetail> findByUserAndBoardGame(BoardGameUser user, BoardGame boardGame);

}
