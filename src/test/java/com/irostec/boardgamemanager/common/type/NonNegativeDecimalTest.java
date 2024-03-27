package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NonNegativeDecimalTest
 * Verifies that NonNegativeDecimal instances behave as expected
 */
class NonNegativeDecimalTest {

    private static final String PROPERTY_NAME = "nonNegativeDecimalProperty";

    @Test
    void givenANonNegativeDecimal_whenItIsInitializedWithANegativeInput_thenTheValidationShouldFail() {

        final float input = -1.5f;

        final Validation<String, NonNegativeDecimal> result =
                NonNegativeDecimal.of(PROPERTY_NAME, input);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenANonNegativeDecimal_whenItIsInitializedWithZero_thenTheValidationShouldSucceed() {

        final float input = 0;

        final Validation<String, NonNegativeDecimal> result = NonNegativeDecimal.of(PROPERTY_NAME, input);

        assertTrue(result.isValid());
        assertEquals(input, result.get().getValue());

    }

    @Test
    void givenANonNegativeDecimal_whenItIsInitializedWithAPositiveInput_thenTheValidationShouldSucceed() {

        final float input = 1.0f;

        final Validation<String, NonNegativeDecimal> result = NonNegativeDecimal.of(PROPERTY_NAME, input);

        assertTrue(result.isValid());
        assertEquals(input, result.get().getValue());

    }

}
