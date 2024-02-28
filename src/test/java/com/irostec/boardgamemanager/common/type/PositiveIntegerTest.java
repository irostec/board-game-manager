package com.irostec.boardgamemanager.common.type;

import com.irostec.boardgamemanager.common.exception.BGMException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * PositiveIntegerTest
 * Verifies that PositiveInteger instances behave as expected
 */
class PositiveIntegerTest {

    @Test
    void givenAPositiveInteger_whenItIsInitializedWithANegativeInput_thenAnExceptionShouldBeThrown() {

        final int input = -1;

        assertThrows(BGMException.class, () -> new PositiveInteger(input));

    }

    @Test
    void givenAPositiveInteger_whenItIsInitializedWithZero_thenAnExceptionShouldBeThrown()
            throws BGMException{

        final int input = 0;

        assertThrows(BGMException.class, () -> new PositiveInteger(input));

    }

    @Test
    void givenAPositiveInteger_whenItIsInitializedWithAPositiveInput_thenTheInitializationShouldSucceed()
            throws BGMException{

        final int input = 1;

        final PositiveInteger result = new PositiveInteger(input);

        assertEquals(input, result.value());

    }

}
