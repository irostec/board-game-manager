package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameInclusionDetailRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameInclusionRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class BoardGameInclusionService {

    private final Logger logger = LogManager.getLogger(BoardGameInclusionService.class);

    private final BoardGameInclusionRepository boardGameInclusionRepository;
    private final BoardGameInclusionDetailRepository boardGameInclusionDetailRepository;

    public <E> Either<E, BoardGameInclusionDetail> includeBoardGame(
        long boardGameUserId,
        long boardGameId,
        String reason,
        Function<Throwable, E> exceptionToError
    ) {

        final String inputDescription = String.format("for board game user id '%d' and board game id '%d' and reason '%s'", boardGameUserId, boardGameId, reason);

        logger.info("Creating single board game inclusion " + inputDescription);

        Either<E, BoardGameInclusionDetail> result = createBoardGameInclusion(reason, exceptionToError)
            .map(BoardGameInclusion::getId)
            .flatMap(boardGameInclusionId -> createBoardGameInclusionDetail(boardGameUserId, boardGameId, boardGameInclusionId, exceptionToError));

        result
            .peek(boardGameInclusionDetail -> logger.info("Successfully created single board game inclusion " + inputDescription))
            .peekLeft(error -> logger.info("Error creating board game inclusion " + inputDescription));

        return result;

    }

    private <E> Either<E, BoardGameInclusion> createBoardGameInclusion(
        String reason,
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            () -> {
                BoardGameInclusion boardGameInclusion = new BoardGameInclusion();
                boardGameInclusion.setReason(reason);

                return boardGameInclusionRepository.save(boardGameInclusion);
            },
            exceptionToError
        );

    }

    private <E> Either<E, BoardGameInclusionDetail> createBoardGameInclusionDetail(
        long boardGameUserId,
        long boardGameId,
        long boardGameInclusionId,
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            () -> {

                BoardGameInclusionDetail boardGameInclusionDetail = new BoardGameInclusionDetail();
                boardGameInclusionDetail.setBoardGameUserId(boardGameUserId);
                boardGameInclusionDetail.setBoardGameId(boardGameId);
                boardGameInclusionDetail.setBoardGameInclusionId(boardGameInclusionId);

                return boardGameInclusionDetailRepository.save(boardGameInclusionDetail);
            },
            exceptionToError
        );

    }

}
