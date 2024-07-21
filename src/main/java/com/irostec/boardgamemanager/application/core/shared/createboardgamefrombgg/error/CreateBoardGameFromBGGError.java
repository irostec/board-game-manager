package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;

public sealed interface CreateBoardGameFromBGGError
    permits GetLoggedUsernameError, BGGApiError, BoardGameAlreadyExists, DatabaseError, UserNotFound {
}
