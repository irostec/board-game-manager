package com.irostec.boardgamemanager.application.core;

import com.irostec.boardgamemanager.application.core.api.transaction.BasicTransaction1;
import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.BoardGameCreationError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.BoardGameInclusionError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.CreateAndIncludeBoardGameFromBGGError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.UserRetrievalError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.input.RequestToCreateAndIncludeBoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.output.CreateAndIncludeBoardGameFromBGGServiceResult;
import com.irostec.boardgamemanager.application.core.shared.CreateBoardGameFromBGGService;
import com.irostec.boardgamemanager.application.core.shared.GetCurrentUserService;
import com.irostec.boardgamemanager.application.core.shared.IncludeBoardGameService;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.input.BoardGameCreationRequest;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.output.User;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;
import io.vavr.control.Either;

public class CreateAndIncludeBoardGameFromBGGService
    extends BasicTransaction1<CreateAndIncludeBoardGameFromBGGError, CreateAndIncludeBoardGameFromBGGServiceResult, RequestToCreateAndIncludeBoardGameFromBGG> {

    private final GetCurrentUserService getCurrentUserService;
    private final CreateBoardGameFromBGGService createBoardGameFromBGGService;
    private final IncludeBoardGameService includeBoardGameService;

    public CreateAndIncludeBoardGameFromBGGService(
        TransactionalOperationFactory transactionalOperationFactory,
        GetCurrentUserService getCurrentUserService,
        CreateBoardGameFromBGGService createBoardGameFromBGGService,
        IncludeBoardGameService includeBoardGameService
    ) {

        super(transactionalOperationFactory);
        this.getCurrentUserService = getCurrentUserService;
        this.createBoardGameFromBGGService = createBoardGameFromBGGService;
        this.includeBoardGameService = includeBoardGameService;

    }

    @Override
    protected Either<CreateAndIncludeBoardGameFromBGGError, CreateAndIncludeBoardGameFromBGGServiceResult> baseOperation(
        RequestToCreateAndIncludeBoardGameFromBGG request
    ) {

        Either<CreateAndIncludeBoardGameFromBGGError, User>  currentUserContainer =
            getCurrentUserService.execute().mapLeft(UserRetrievalError::new);

        Either<CreateAndIncludeBoardGameFromBGGError, BoardGameInclusionResult> boardGameInclusionResultContainer =
            currentUserContainer.flatMap(currentUser -> {

                long userId = currentUser.id();

                Either<CreateAndIncludeBoardGameFromBGGError, BoardGameSummary> boardGameSummaryContainer =
                    createBoardGameFromBGGService.execute(new BoardGameCreationRequest(request.externalId(), userId))
                    .mapLeft(BoardGameCreationError::new);

                return boardGameSummaryContainer.map(boardGameSummary ->
                    new RequestToIncludeBoardGame(userId, boardGameSummary.id(), request.reasonForInclusion())
                )
                .flatMap(requestToIncludeBoardGame ->
                    includeBoardGameService.execute(requestToIncludeBoardGame)
                        .mapLeft(BoardGameInclusionError::new)
                );

            });

        return boardGameInclusionResultContainer.map(
            boardGameInclusionResult ->
                new CreateAndIncludeBoardGameFromBGGServiceResult(
                    boardGameInclusionResult.boardGameId(),
                    boardGameInclusionResult.boardGameInclusionId()
                )
        );

    }

}
