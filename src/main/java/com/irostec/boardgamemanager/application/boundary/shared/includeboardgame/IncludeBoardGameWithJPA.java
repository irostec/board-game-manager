package com.irostec.boardgamemanager.application.boundary.shared.includeboardgame;

import com.irostec.boardgamemanager.application.boundary.api.jpa.service.BoardGameInclusionService;
import com.irostec.boardgamemanager.application.core.api.transaction.BasicTransaction1;
import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import com.irostec.boardgamemanager.application.core.shared.IncludeBoardGameService;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGError;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.input.BoardGameCreationRequest;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.error.IncludeBoardGameError;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.error.DatabaseError;

import java.util.function.Function;

@Component
class IncludeBoardGameWithJPA
    extends BasicTransaction1<IncludeBoardGameError, BoardGameInclusionResult, RequestToIncludeBoardGame>
    implements IncludeBoardGameService {

    private static final Function<Throwable, IncludeBoardGameError> EXCEPTION_To_ERROR = DatabaseError::new;

    private final BoardGameInclusionService boardGameInclusionService;

    IncludeBoardGameWithJPA(
        TransactionalOperationFactory transactionalOperationFactory,
        BoardGameInclusionService boardGameInclusionService
    ) {
        super(transactionalOperationFactory);
        this.boardGameInclusionService = boardGameInclusionService;
    }

    @Override
    protected Either<IncludeBoardGameError, BoardGameInclusionResult> baseOperation(RequestToIncludeBoardGame requestToIncludeBoardGame) {

        return boardGameInclusionService.includeBoardGame(
                requestToIncludeBoardGame.userId(),
                requestToIncludeBoardGame.boardGameId(),
                requestToIncludeBoardGame.reason(),
                EXCEPTION_To_ERROR
        )
        .map(boardGameInclusionDetail ->
            new BoardGameInclusionResult(
                boardGameInclusionDetail.getBoardGameId(),
                boardGameInclusionDetail.getBoardGameInclusionId()
            )
        );

    }

}
