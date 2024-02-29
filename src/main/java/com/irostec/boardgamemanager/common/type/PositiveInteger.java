package com.irostec.boardgamemanager.common.type;

import com.irostec.boardgamemanager.common.exception.BGMException;

/**
 * PositiveInteger
 * A wrapper for integers that must be positive, like the number of players in a board game or the playing time
 */
public class PositiveInteger {

    private final int value;

    public PositiveInteger(int value) throws BGMException {

        if (value <= 0)
            throw new BGMException("The provided value should be greater than 0: " + value);

        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
