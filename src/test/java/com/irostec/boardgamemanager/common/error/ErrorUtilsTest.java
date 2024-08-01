package com.irostec.boardgamemanager.common.error;

import com.irostec.boardgamemanager.common.type.PositiveShort;
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

        final Validation<String, PositiveShort> invalidPositiveInteger1 = PositiveShort.of("invalidPositiveInteger1", -1);
        final Validation<String, PositiveShort> invalidPositiveInteger2 = PositiveShort.of("invalidPositiveInteger2", 0);
        final Validation<String, PositiveShort> validPositiveInteger1 = PositiveShort.of("validPositiveInteger1", 1);
        final Validation<String, PositiveShort> validPositiveInteger2 = PositiveShort.of("validPositiveInteger2", Short.MAX_VALUE);

        final Stream<Validation<String, PositiveShort>> validations =
                List.of(
                        invalidPositiveInteger1,
                        invalidPositiveInteger2,
                        validPositiveInteger1,
                        validPositiveInteger2
                )
                .stream();

        final Validation<List<String>, List<PositiveShort>> result = ErrorUtils.sequence(validations);

        assertTrue(result.isInvalid());
        final List<String> expectedErrorMessages =
                List.of(invalidPositiveInteger1.getError(), invalidPositiveInteger2.getError());
        assertEquals(expectedErrorMessages, result.getError());

    }

    @Test
    void testSequenceWithoutErrors() {

        final Validation<String, PositiveShort> validPositiveInteger1 = PositiveShort.of("validPositiveInteger1", 1);
        final Validation<String, PositiveShort> validPositiveInteger2 = PositiveShort.of("validPositiveInteger2", Short.MAX_VALUE);

        final Stream<Validation<String, PositiveShort>> validations =
                List.of(validPositiveInteger1, validPositiveInteger2).stream();

        final Validation<List<String>, List<PositiveShort>> result = ErrorUtils.sequence(validations);

        assertTrue(result.isValid());
        final List<PositiveShort> expectedValues = List.of(validPositiveInteger1.get(), validPositiveInteger2.get());
        assertEquals(expectedValues, result.get());

    }

}
