package com.irostec.boardgamemanager.application.shared;

import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;

/**
 * BGGApi
 * A representation of all the useful operations exposed by the boardgamegeek.com API
 */
public interface BGGApi {

    BoardGameFromBGG execute(String externalId) throws BGMException;

}
