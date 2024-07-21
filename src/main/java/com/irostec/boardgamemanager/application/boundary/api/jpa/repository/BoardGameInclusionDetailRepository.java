package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardGameInclusionDetailRepository  extends JpaRepository<BoardGameInclusionDetail, Long> {

    Optional<BoardGameInclusionDetail> findByBoardGameUserIdAndBoardGameId(long boardGameUserId, long boardGameId);

}
