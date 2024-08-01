package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameUser;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameInclusionDetailRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameInclusionRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameUserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
public class BoardGameInclusionService {

    private final Logger logger = LogManager.getLogger(BoardGameInclusionService.class);

    private final BoardGameInclusionRepository boardGameInclusionRepository;
    private final BoardGameInclusionDetailRepository boardGameInclusionDetailRepository;
    private final BoardGameRepository boardGameRepository;
    private final BoardGameUserRepository boardGameUserRepository;

    @Transactional
    public BoardGameInclusionDetail includeBoardGame(
        long userId,
        long boardGameId,
        String reason
    ) {

        final String inputDescription = String.format(
            "for board game user id '%d' and board game id '%d' and reason '%s'",
            userId,
            boardGameId,
            reason
        );
        logger.info("Creating single board game inclusion " + inputDescription);

        BoardGameUser user = boardGameUserRepository.getReferenceById(userId);
        BoardGame boardGame = boardGameRepository.getReferenceById(boardGameId);

        Optional<BoardGameInclusionDetail> optionalBoardGameInclusionDetail =
            boardGameInclusionDetailRepository.findByUserAndBoardGame(user, boardGame);

        return optionalBoardGameInclusionDetail.orElseGet(() ->
            createBoardGameInclusionAndDetail(user, boardGame, reason, inputDescription)
        );

    }

    private BoardGameInclusionDetail createBoardGameInclusionAndDetail(
        BoardGameUser user,
        BoardGame boardGame,
        String reason,
        String inputDescription
    ) {

        BoardGameInclusion boardGameInclusion = createBoardGameInclusion(reason);
        BoardGameInclusionDetail boardGameInclusionDetail =
                createBoardGameInclusionDetail(user, boardGame, boardGameInclusion);

        logger.info("Successfully created single board game inclusion " + inputDescription);

        return boardGameInclusionDetail;

    }

    private BoardGameInclusion createBoardGameInclusion(String reason) {

        BoardGameInclusion boardGameInclusion = new BoardGameInclusion();
        boardGameInclusion.setReason(reason);

        return boardGameInclusionRepository.save(boardGameInclusion);

    }

    private BoardGameInclusionDetail createBoardGameInclusionDetail(
        BoardGameUser user,
        BoardGame boardGame,
        BoardGameInclusion boardGameInclusion
    ) {

        BoardGameInclusionDetail boardGameInclusionDetail = new BoardGameInclusionDetail();
        boardGameInclusionDetail.setUser(user);
        boardGameInclusionDetail.setBoardGame(boardGame);
        boardGameInclusionDetail.setBoardGameInclusion(boardGameInclusion);

        return boardGameInclusionDetailRepository.save(boardGameInclusionDetail);

    }

}
