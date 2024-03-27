package com.irostec.boardgamemanager.application;

import com.irostec.boardgamemanager.application.getboardgamefrombgg.helper.BoardGameFromBGGMapper;
import com.irostec.boardgamemanager.application.getboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.application.shared.bggapi.error.BGGApiError;
import io.vavr.control.Either;

/**
 * GetBoardGameFromBGGService
 * Use case to retrieve information of a board game from boardgamegeek.com
 */
public class GetBoardGameFromBGGService {

    private final BGGApi bggApi;

    public GetBoardGameFromBGGService(BGGApi bggApi) {
        this.bggApi = bggApi;
    }

    public Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> execute(String externalId) {

        return bggApi.execute(externalId)
                .map(BoardGameFromBGGMapper.INSTANCE::toBoardGameFromBGGWithPartitionedLinks);

    }

}
