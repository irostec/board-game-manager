package com.irostec.boardgamemanager.application.boundary.shared.includeboardgame;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import com.irostec.boardgamemanager.application.boundary.api.jpa.service.BoardGameInclusionService;
import com.irostec.boardgamemanager.application.core.shared.IncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.exception.BoundaryException;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.exception.IncludeBoardGameException;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class IncludeBoardGameWithJPA
    implements IncludeBoardGame {

    private final BoardGameInclusionService boardGameInclusionService;

    @Override
    public BoardGameInclusionResult execute(RequestToIncludeBoardGame requestToIncludeBoardGame)
        throws IncludeBoardGameException {

        try {

            BoardGameInclusionDetail boardGameInclusionDetail = boardGameInclusionService.includeBoardGame(
                    requestToIncludeBoardGame.userId(),
                    requestToIncludeBoardGame.boardGameId(),
                    requestToIncludeBoardGame.reason()
            );

            return new BoardGameInclusionResult(
                    boardGameInclusionDetail.getBoardGameId(),
                    boardGameInclusionDetail.getBoardGameInclusionId()
            );

        }
        catch (com.irostec.boardgamemanager.common.error.BoundaryException boundaryException) {
            throw new BoundaryException(boundaryException);
        }

    }

}
