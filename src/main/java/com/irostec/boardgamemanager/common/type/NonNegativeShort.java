package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NonNegativeByte
 * A wrapper for bytes that cannot be negative, like human ages
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NonNegativeShort {

    private final short value;

    public static Validation<String, NonNegativeShort> of(String propertyName, short value) {

        return (value < 0) ?
                Validation.invalid(
                        String.format("The value provided for the property '%s' should be equal to or greater than 0, but it's %d.", propertyName, value)
                ) :
                Validation.valid(new NonNegativeShort(value));

    }

}
