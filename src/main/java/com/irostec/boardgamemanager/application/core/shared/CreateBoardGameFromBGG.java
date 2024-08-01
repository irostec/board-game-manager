package com.irostec.boardgamemanager.application.core.shared;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.BGGApiException;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGException;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.input.BoardGameCreationRequest;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateBoardGameFromBGG {

    private final BGGApi bggApi;
    private final SaveBGGBoardGameFixedData saveBGGBoardGameFixedData;

    public BoardGameSummary execute(BoardGameCreationRequest boardGameCreationRequest)
        throws CreateBoardGameFromBGGException {

        BoardGameFromBGG boardGameFromBGG = bggApi.execute(boardGameCreationRequest.externalId())
            .getOrElseThrow(BGGApiException::new);

        return saveBGGBoardGameFixedData.execute(boardGameFromBGG);

    }

}
