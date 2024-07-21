package com.irostec.boardgamemanager.application.boundary.shared.bggapi;

import com.irostec.boardgamemanager.application.boundary.utility.RetrofitUtils;
import com.irostec.boardgamemanager.application.core.shared.BGGApi;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BoardGameNotFound;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.ExternalServiceFailure;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.common.utility.Logging;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.irostec.boardgamemanager.application.boundary.shared.bggapi.jaxb.generated.Items;

import java.util.Objects;

/**
 * BGGXmlApi
 * Standard implementation of BGGApi
 */
@Service
final class BGGXmlApi implements BGGApi {

    private final Logger logger = LogManager.getLogger(BGGXmlApi.class);

    private final Endpoints bggEndpoints;

    public BGGXmlApi(Endpoints bggEndpoints) {
        this.bggEndpoints = bggEndpoints;
    }

    @Override
    public Either<BGGApiError, BoardGameFromBGG> execute(String externalId) {

        Logging.info(logger, "execute", String.format("Searching for a board game with id '%s' in boardgamegeek.com", externalId));

        Either<BGGApiError, Items> callResult = RetrofitUtils.handleCall(bggEndpoints.getBoardGamesById(externalId, "boardgame", "1", "1"))
                .mapLeft(httpError -> new ExternalServiceFailure(externalId, httpError));

        callResult
            .peek(items -> logger.info(String.format("Successfully searched for a board game with id '%s' in boardgamegeek.com", externalId)))
            .peekLeft(error -> logger.info(String.format("Error searching for a board game with id '%s' in boardgamegeek.com", externalId)));

        return callResult
                .flatMap(items ->
                        Objects.isNull(items.getItem()) || items.getItem().isEmpty() ?
                                Either.left(new BoardGameNotFound(externalId)) :
                                Either.right(items.getItem().getFirst())
                )
                .map(ItemsMapper::itemToBoardGame)
                .map(Validation::toEither)
                .flatMap(Either::narrow);

    }

}
