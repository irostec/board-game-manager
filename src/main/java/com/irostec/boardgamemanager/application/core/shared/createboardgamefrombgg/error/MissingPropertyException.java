package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;

import com.irostec.boardgamemanager.common.error.RequiredValueNotFoundException;

public final class MissingPropertyException extends CreateBoardGameFromBGGException {

    private final RequiredValueNotFoundException cause;

    public MissingPropertyException(RequiredValueNotFoundException cause) {
        this.cause = cause;
    }

}
