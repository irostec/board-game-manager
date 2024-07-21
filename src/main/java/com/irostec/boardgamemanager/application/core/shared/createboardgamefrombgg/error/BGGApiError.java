package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;

public record BGGApiError(com.irostec.boardgamemanager.application.core.shared.bggapi.error.BGGApiError source)
implements CreateBoardGameFromBGGError {
}
