package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;

import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BGGApiError;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class BGGApiException
    extends CreateBoardGameFromBGGException
{

    private final BGGApiError source;

}
