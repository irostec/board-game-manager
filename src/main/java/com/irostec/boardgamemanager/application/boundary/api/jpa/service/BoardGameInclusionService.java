package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameInclusionDetailRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameInclusionRepository;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class BoardGameInclusionService {

    private final Logger logger = LogManager.getLogger(BoardGameInclusionService.class);

    private final BoardGameInclusionRepository boardGameInclusionRepository;
    private final BoardGameInclusionDetailRepository boardGameInclusionDetailRepository;

    public BoardGameInclusionDetail includeBoardGame(
        long boardGameUserId,
        long boardGameId,
        String reason
    ) throws BoundaryException {

        final String inputDescription = String.format("for board game user id '%d' and board game id '%d' and reason '%s'", boardGameUserId, boardGameId, reason);
        logger.info("Creating single board game inclusion " + inputDescription);

        BoardGameInclusion boardGameInclusion = createBoardGameInclusion(reason);
        BoardGameInclusionDetail boardGameInclusionDetail =
            createBoardGameInclusionDetail(boardGameUserId, boardGameId, boardGameInclusion.getId());

        logger.info("Successfully created single board game inclusion " + inputDescription);

        return boardGameInclusionDetail;

    }

    private BoardGameInclusion createBoardGameInclusion(
        String reason
    ) throws BoundaryException {

        BoardGameInclusion boardGameInclusion = new BoardGameInclusion();
        boardGameInclusion.setReason(reason);

        return wrapWithErrorHandling(
            () -> boardGameInclusionRepository.save(boardGameInclusion)
        );

    }

    private BoardGameInclusionDetail createBoardGameInclusionDetail(
        long boardGameUserId,
        long boardGameId,
        long boardGameInclusionId
    ) throws BoundaryException {

        BoardGameInclusionDetail boardGameInclusionDetail = new BoardGameInclusionDetail();
        boardGameInclusionDetail.setBoardGameUserId(boardGameUserId);
        boardGameInclusionDetail.setBoardGameId(boardGameId);
        boardGameInclusionDetail.setBoardGameInclusionId(boardGameInclusionId);

        return wrapWithErrorHandling(
            () -> boardGameInclusionDetailRepository.save(boardGameInclusionDetail)
        );

    }

}
