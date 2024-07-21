package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;

public record GetLoggedUsernameError(com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.GetCurrentUserError getCurrentUserError)
    implements CreateBoardGameFromBGGError {}
