package com.irostec.boardgamemanager.common.type;

import com.irostec.boardgamemanager.common.exception.BGMException;

/**
 * NonNegativeDecimal
 * A wrapper for floating point numbers that cannot be negative, like weights and the average of a group of ratings
 */
public class NonNegativeDecimal {

    private final float value;

    public NonNegativeDecimal(float value) throws BGMException {

        if (value < 0)
            throw new BGMException("The provided value should be equal to or greater than 0: " + value);

        this.value = value;

    }

    public float value() {
        return this.value;
    }

}
