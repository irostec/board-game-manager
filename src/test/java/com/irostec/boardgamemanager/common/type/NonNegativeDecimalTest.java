package com.irostec.boardgamemanager.common.type;

import com.irostec.boardgamemanager.common.exception.BGMException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * NonNegativeDecimalTest
 * Verifies that NonNegativeDecimal instances behave as expected
 */
class NonNegativeDecimalTest {

    @Test
    void givenANonNegativeDecimal_whenItIsInitializedWithANegativeInput_thenAnExceptionShouldBeThrown() {

        final float input = -1.5f;

        assertThrows(BGMException.class, () -> new NonNegativeDecimal(input));

    }

    @Test
    void givenANonNegativeDecimal_whenItIsInitializedWithZero_thenTheInitializationShouldSucceed()
            throws BGMException{

        final float input = 0;

        final NonNegativeDecimal result = new NonNegativeDecimal(input);

        assertEquals(input, result.value());

    }

    @Test
    void givenANonNegativeDecimal_whenItIsInitializedWithAPositiveInput_thenTheInitializationShouldSucceed()
            throws BGMException{

        final float input = 1.0f;

        final NonNegativeDecimal result = new NonNegativeDecimal(input);

        assertEquals(input, result.value());

    }

}
