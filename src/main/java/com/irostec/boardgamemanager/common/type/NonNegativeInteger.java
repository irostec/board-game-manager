package com.irostec.boardgamemanager.common.type;

import com.irostec.boardgamemanager.common.exception.BGMException;
import lombok.Getter;

/**
 * NonNegativeInteger
 * A wrapper for integers that cannot be negative, like counts and ages
 */
@Getter
public final class NonNegativeInteger {

    private final int value;

    public NonNegativeInteger(int value) throws BGMException {

        if (value < 0)
            throw new BGMException("The provided value should be equal to or greater than 0: " + value);

        this.value = value;
    }

}
