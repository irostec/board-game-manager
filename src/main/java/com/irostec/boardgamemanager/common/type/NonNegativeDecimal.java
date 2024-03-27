package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NonNegativeDecimal
 * A wrapper for floating point numbers that cannot be negative, like weights and the average of a group of ratings
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NonNegativeDecimal {

    private final float value;

    public static Validation<String, NonNegativeDecimal> of(String propertyName, float value) {

        return value < 0 ?
            Validation.invalid(
                            String.format("The value provided for the property '%s' should be equal to or greater than 0, but it's %f.", propertyName, value)
            ) :
            Validation.valid(new NonNegativeDecimal(value));

    }

}
