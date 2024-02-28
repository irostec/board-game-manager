package com.irostec.boardgamemanager.application;

import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper.BoardGameFromBGGMapper;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.common.exception.BGMException;

/**
 * FetchBoardGameFromBGG
 * Use case to retrieve information of a board game from boardgamegeek.com
 */
public class FetchBoardGameFromBGG {

    private final BGGApi bggApi;

    public FetchBoardGameFromBGG(BGGApi bggApi) {
        this.bggApi = bggApi;
    }

    public BoardGameFromBGGWithPartitionedLinks execute(String externalId) throws BGMException {

        final BoardGameFromBGG boardGameFromBGG = bggApi.execute(externalId);

        return BoardGameFromBGGMapper.INSTANCE.toBoardGameFromBGGWithPartitionedLinks(boardGameFromBGG);

    }

}
