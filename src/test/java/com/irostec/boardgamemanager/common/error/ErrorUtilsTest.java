package com.irostec.boardgamemanager.common.error;

import com.irostec.boardgamemanager.common.type.PositiveInteger;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ErrorUtilsTest
 */
class ErrorUtilsTest {

    @Test
    void testSequenceWithErrors() {

        final Validation<String, PositiveInteger> invalidPositiveInteger1 = PositiveInteger.of("invalidPositiveInteger1", -1);
        final Validation<String, PositiveInteger> invalidPositiveInteger2 = PositiveInteger.of("invalidPositiveInteger2", 0);
        final Validation<String, PositiveInteger> validPositiveInteger1 = PositiveInteger.of("validPositiveInteger1", 1);
        final Validation<String, PositiveInteger> validPositiveInteger2 = PositiveInteger.of("validPositiveInteger2", Integer.MAX_VALUE);

        final Stream<Validation<String, PositiveInteger>> validations =
                List.of(
                        invalidPositiveInteger1,
                        invalidPositiveInteger2,
                        validPositiveInteger1,
                        validPositiveInteger2
                )
                .stream();

        final Validation<List<String>, List<PositiveInteger>> result = ErrorUtils.sequence(validations);

        assertTrue(result.isInvalid());
        final List<String> expectedErrorMessages =
                List.of(invalidPositiveInteger1.getError(), invalidPositiveInteger2.getError());
        assertEquals(expectedErrorMessages, result.getError());

    }

    @Test
    void testSequenceWithoutErrors() {

        final Validation<String, PositiveInteger> validPositiveInteger1 = PositiveInteger.of("validPositiveInteger1", 1);
        final Validation<String, PositiveInteger> validPositiveInteger2 = PositiveInteger.of("validPositiveInteger2", Integer.MAX_VALUE);

        final Stream<Validation<String, PositiveInteger>> validations =
                List.of(validPositiveInteger1, validPositiveInteger2).stream();

        final Validation<List<String>, List<PositiveInteger>> result = ErrorUtils.sequence(validations);

        assertTrue(result.isValid());
        final List<PositiveInteger> expectedValues = List.of(validPositiveInteger1.get(), validPositiveInteger2.get());
        assertEquals(expectedValues, result.get());

    }

}
