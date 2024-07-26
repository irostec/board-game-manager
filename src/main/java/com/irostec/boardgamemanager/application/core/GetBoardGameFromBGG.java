package com.irostec.boardgamemanager.application.core;

import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.helper.BoardGameFromBGGMapper;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.core.shared.BGGApi;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BGGApiError;
import io.vavr.control.Either;

/**
 * GetBoardGameFromBGGService
 * Use case to retrieve information of a board game from boardgamegeek.com
 */
public class GetBoardGameFromBGG {

    private final BGGApi bggApi;

    public GetBoardGameFromBGG(BGGApi bggApi) {
        this.bggApi = bggApi;
    }

    public Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> execute(String externalId) {

        return bggApi.execute(externalId)
                .map(BoardGameFromBGGMapper.INSTANCE::toBoardGameFromBGGWithPartitionedLinks);

    }

}
