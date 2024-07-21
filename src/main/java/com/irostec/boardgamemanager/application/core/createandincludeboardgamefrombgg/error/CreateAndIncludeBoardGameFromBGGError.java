package com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error;

public sealed interface CreateAndIncludeBoardGameFromBGGError
    permits UserRetrievalError, BoardGameCreationError, BoardGameInclusionError {}
