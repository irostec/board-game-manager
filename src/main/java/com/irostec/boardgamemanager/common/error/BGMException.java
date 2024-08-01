package com.irostec.boardgamemanager.common.error;

public abstract sealed class BGMException extends RuntimeException permits BoundaryException, RequiredValueNotFoundException {

    protected BGMException(Throwable cause) {
        super(cause);
    }

    protected BGMException(String message) {
        super(message);
    }

}
