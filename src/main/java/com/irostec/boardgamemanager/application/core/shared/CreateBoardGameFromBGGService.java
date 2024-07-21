package com.irostec.boardgamemanager.application.core.shared;

import com.irostec.boardgamemanager.application.core.api.transaction.BasicTransaction1;
import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.BGGApiError;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGError;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.input.BoardGameCreationRequest;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import io.vavr.control.Either;

public class CreateBoardGameFromBGGService
    extends BasicTransaction1<CreateBoardGameFromBGGError, BoardGameSummary, BoardGameCreationRequest> {

    private final BGGApi bggApi;
    private final SaveBGGBoardGameFixedData saveBGGBoardGameFixedData;

    public CreateBoardGameFromBGGService(
        TransactionalOperationFactory transactionalOperationFactory,
        BGGApi bggApi,
        SaveBGGBoardGameFixedData saveBGGBoardGameFixedData
    ) {

        super(transactionalOperationFactory);
        this.bggApi = bggApi;
        this.saveBGGBoardGameFixedData = saveBGGBoardGameFixedData;

    }

    @Override
    protected Either<CreateBoardGameFromBGGError, BoardGameSummary> baseOperation(BoardGameCreationRequest boardGameCreationRequest) {

        return bggApi
            .execute(boardGameCreationRequest.externalId())
            .<CreateBoardGameFromBGGError>mapLeft(BGGApiError::new)
            .flatMap(saveBGGBoardGameFixedData::execute);

    }

}
