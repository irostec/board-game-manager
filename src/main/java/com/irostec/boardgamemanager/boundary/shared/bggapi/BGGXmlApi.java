package com.irostec.boardgamemanager.boundary.shared.bggapi;

import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.application.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.shared.bggapi.error.BoardGameNotFound;
import com.irostec.boardgamemanager.application.shared.bggapi.error.ExternalServiceFailure;
import com.irostec.boardgamemanager.boundary.shared.Endpoints;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.irostec.boardgamemanager.boundary.shared.bggapi.jaxb.generated.Items;

import java.util.Objects;

import static com.irostec.boardgamemanager.boundary.utility.RetrofitUtils.handleCall;

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

        LoggingUtils.info(logger, "execute", String.format("Attempting to find a board game by id '%s' in boardgamegeek.com", externalId));

        Either<BGGApiError, Items> callResult = handleCall(bggEndpoints.getBoardGamesById(externalId, "boardgame", "1", "1"))
                .mapLeft(httpError -> new ExternalServiceFailure(externalId, httpError));

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
