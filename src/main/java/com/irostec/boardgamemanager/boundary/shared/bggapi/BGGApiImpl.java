package com.irostec.boardgamemanager.boundary.shared.bggapi;

import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.boundary.shared.Endpoints;
import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.common.exception.NotFoundException;
import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.irostec.boardgamemanager.boundary.utility.RetrofitUtils.handleCall;

/**
 * BGGApiImpl
 * Standard implementation of BGGApi
 */
@Service
final class BGGApiImpl implements BGGApi {

    private final Logger logger = LogManager.getLogger(BGGApiImpl.class);

    private final Endpoints bggEndpoints;

    public BGGApiImpl(Endpoints bggEndpoints) {
        this.bggEndpoints = bggEndpoints;
    }

    @Override
    public BoardGameFromBGG execute(String externalId) throws BGMException {

        LoggingUtils.info(logger, "execute", String.format("Attempting to find a board game by id '%s' in boardgamegeek.com", externalId));

        return ItemsMapper.itemsToBoardGames(handleCall(bggEndpoints.getBoardGamesById(externalId, "boardgame", "1", "1")))
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No board game with id '%s' was found in boardgamegeek.com", externalId)));

    }

}
