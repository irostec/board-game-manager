package com.irostec.boardgamemanager.common.type;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PositiveIntegerTest
 * Verifies that PositiveInteger instances behave as expected
 */
class PositiveShortTest {

    private static final String PROPERTY_NAME = "positiveIntegerProperty";

    @Test
    void givenAPositiveInteger_whenItIsInitializedWithANegativeInput_thenTheValidationShouldFail() {

        final int input = -1;

        final Validation<String, PositiveShort> result = PositiveShort.of(PROPERTY_NAME, input);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAPositiveInteger_whenItIsInitializedWithZero_thenTheValidationShouldFail() {

        final int input = 0;

        final Validation<String, PositiveShort> result = PositiveShort.of(PROPERTY_NAME, input);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAPositiveInteger_whenItIsInitializedWithAPositiveInput_thenTheInitializationShouldSucceed() {

        final int input = 1;

        final Validation<String, PositiveShort> result = PositiveShort.of(PROPERTY_NAME, input);

        assertTrue(result.isValid());
        assertEquals(input, result.get().getValue());

    }

}
