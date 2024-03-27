package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NonNegativeIntegerTest
 * Verifies that NonNegativeInteger instances behave as expected
 */
class NonNegativeIntegerTest {

    private static final String PROPERTY_NAME = "nonNegativeIntegerProperty";

    @Test
    void givenANonNegativeInteger_whenItIsInitializedWithANegativeInput_thenTheValidationShouldFail() {

        final int input = -1;

        final Validation<String, NonNegativeInteger> result = NonNegativeInteger.of(PROPERTY_NAME, input);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenANonNegativeInteger_whenItIsInitializedWithZero_thenTheValidationShouldSucceed() {

        final int input = 0;

        final Validation<String, NonNegativeInteger> result = NonNegativeInteger.of(PROPERTY_NAME, input);

        assertTrue(result.isValid());
        assertEquals(input, result.get().getValue());

    }

    @Test
    void givenANonNegativeInteger_whenItIsInitializedWithAPositiveInput_thenTheValidationShouldSucceed() {

        final int input = 1;

        final Validation<String, NonNegativeInteger> result = NonNegativeInteger.of(PROPERTY_NAME, input);

        assertTrue(result.isValid());
        assertEquals(input, result.get().getValue());

    }

}
