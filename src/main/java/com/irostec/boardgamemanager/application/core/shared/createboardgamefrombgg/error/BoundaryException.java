package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;


public final class BoundaryException extends CreateBoardGameFromBGGException {

    private final com.irostec.boardgamemanager.common.error.BoundaryException cause;

    public BoundaryException(com.irostec.boardgamemanager.common.error.BoundaryException cause) {
        this.cause = cause;
    }

}
