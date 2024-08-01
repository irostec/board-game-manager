package com.irostec.boardgamemanager.application.boundary.shared.includeboardgame;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameInclusionDetail;
import com.irostec.boardgamemanager.application.boundary.api.jpa.service.BoardGameInclusionService;
import com.irostec.boardgamemanager.application.core.shared.IncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
class IncludeBoardGameWithJPA
    implements IncludeBoardGame {

    private final BoardGameInclusionService boardGameInclusionService;

    @Transactional
    @Override
    public BoardGameInclusionResult execute(RequestToIncludeBoardGame requestToIncludeBoardGame) {

        BoardGameInclusionDetail boardGameInclusionDetail = boardGameInclusionService.includeBoardGame(
            requestToIncludeBoardGame.userId(),
            requestToIncludeBoardGame.boardGameId(),
            requestToIncludeBoardGame.reason()
        );

        return new BoardGameInclusionResult(
            boardGameInclusionDetail.getBoardGame().getId(),
            boardGameInclusionDetail.getBoardGameInclusion().getId()
        );

    }

}
