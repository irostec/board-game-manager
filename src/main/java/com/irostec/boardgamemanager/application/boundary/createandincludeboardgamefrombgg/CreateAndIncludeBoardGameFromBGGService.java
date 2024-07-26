package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg;

import com.irostec.boardgamemanager.application.core.CreateAndIncludeBoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.CreateBoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.GetCurrentUser;
import com.irostec.boardgamemanager.application.core.shared.IncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGException;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.input.BoardGameCreationRequest;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.output.User;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.exception.IncludeBoardGameException;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
class CreateAndIncludeBoardGameFromBGGService implements CreateAndIncludeBoardGameFromBGG {

    private final GetCurrentUser getCurrentUser;
    private final CreateBoardGameFromBGG createBoardGameFromBGG;
    private final IncludeBoardGame includeBoardGame;

    @Transactional
    public CreateAndIncludeBoardGameFromBGG.Output execute(
        CreateAndIncludeBoardGameFromBGG.Input input
    ) throws CreateAndIncludeBoardGameFromBGG.Failure {

        User currentUser = getCurrentUser.execute().getOrElseThrow(UserRetrievalException::new);
        long userId = currentUser.id();

        try {

            BoardGameSummary boardGameSummary = createBoardGameFromBGG.execute(
                new BoardGameCreationRequest(input.externalId(), userId)
            );

            RequestToIncludeBoardGame requestToIncludeBoardGame =
                new RequestToIncludeBoardGame(userId, boardGameSummary.id(), input.reasonForInclusion());

            BoardGameInclusionResult boardGameInclusionResult =
                includeBoardGame.execute(requestToIncludeBoardGame);

            return new CreateAndIncludeBoardGameFromBGG.Output(
                boardGameInclusionResult.boardGameId(),
                boardGameInclusionResult.boardGameInclusionId()
            );

        }
        catch (CreateBoardGameFromBGGException exception) {
            throw new BoardGameCreationException(exception);
        }
        catch (IncludeBoardGameException exception) {
            throw new BoardGameInclusionException(exception);
        }

    }

}
