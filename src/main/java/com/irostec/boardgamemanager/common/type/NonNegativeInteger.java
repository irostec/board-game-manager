package com.irostec.boardgamemanager.common.type;

import com.irostec.boardgamemanager.common.exception.BGMException;

/**
 * NonNegativeInteger
 * A wrapper for integers that cannot be negative, like counts and ages
 */
public final class NonNegativeInteger {

    private final int value;

    public NonNegativeInteger(int value) throws BGMException {

        if (value < 0)
            throw new BGMException("The provided value should be equal to or greater than 0: " + value);

        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
