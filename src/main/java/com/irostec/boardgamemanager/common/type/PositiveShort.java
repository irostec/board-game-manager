package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PositiveShort
 * A wrapper for integers that must be positive, like the number of players in a board game or the playing time
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PositiveShort {

    private final short value;

    public static Validation<String, PositiveShort> of(String propertyName, short value) {

        return value <= 0 ?
            Validation.invalid(
                    String.format("The value provided for property '%s' was expected to be greater than 0, but it's %d.", propertyName, value)
            ) :
            Validation.valid(new PositiveShort(value));

    }

    public static Validation<String, PositiveShort> of(String propertyName, int value) {

        return value > Short.MAX_VALUE ?
            Validation.invalid(
                String.format("The value provided for property '%s' is too big: %d.", propertyName, value)
            ) :
            PositiveShort.of(propertyName, (short) value);

    }

}
