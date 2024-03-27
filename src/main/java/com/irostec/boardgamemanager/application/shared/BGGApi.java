package com.irostec.boardgamemanager.application.shared;

import com.irostec.boardgamemanager.application.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import io.vavr.control.Either;

/**
 * BGGApi
 * A representation of all the useful operations exposed by the boardgamegeek.com API
 */
public interface BGGApi {

    Either<BGGApiError, BoardGameFromBGG> execute(String externalId);

}
