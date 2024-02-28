package com.irostec.boardgamemanager.common.type;


import com.irostec.boardgamemanager.common.exception.BGMException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * NonNegativeIntegerTest
 * Verifies that NonNegativeInteger instances behave as expected
 */
class NonNegativeIntegerTest {

    @Test
    void givenANonNegativeInteger_whenItIsInitializedWithANegativeInput_thenAnExceptionShouldBeThrown() {

        final int input = -1;

        assertThrows(BGMException.class, () -> new NonNegativeInteger(input));

    }

    @Test
    void givenANonNegativeInteger_whenItIsInitializedWithZero_thenTheInitializationShouldSucceed()
    throws BGMException{

        final int input = 0;

        final NonNegativeInteger result = new NonNegativeInteger(input);

        assertEquals(input, result.value());

    }

    @Test
    void givenANonNegativeInteger_whenItIsInitializedWithAPositiveInput_thenTheInitializationShouldSucceed()
            throws BGMException{

        final int input = 1;

        final NonNegativeInteger result = new NonNegativeInteger(input);

        assertEquals(input, result.value());

    }

}
