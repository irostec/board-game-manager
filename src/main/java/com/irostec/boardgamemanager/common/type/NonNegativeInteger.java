package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NonNegativeInteger
 * A wrapper for integers that cannot be negative, like counts and ages
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class NonNegativeInteger {

    private final int value;

    public static Validation<String, NonNegativeInteger> of(String propertyName, int value) {

        return (value < 0) ?
                Validation.invalid(
                        String.format("The value provided for the property '%s' should be equal to or greater than 0, but it's %d.", propertyName, value)
                ) :
                Validation.valid(new NonNegativeInteger(value));

    }

}
