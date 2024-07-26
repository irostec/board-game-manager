package com.irostec.boardgamemanager.application.core.shared.includeboardgame.exception;

public sealed class IncludeBoardGameException extends Exception permits BoundaryException {

    protected IncludeBoardGameException(Throwable cause) {
        super(cause);
    }

}
