package com.irostec.boardgamemanager.common.utility;

import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ExceptionUtilsTest
 * Validates that the functionality provided by ExceptionUtils behaves correctly
 */
class ExceptionUtilsTest {

    private static final Checked.Function<Integer, NonNegativeInteger, BGMException> F = NonNegativeInteger::new;

    private static final Function<Integer, Either<BGMException, NonNegativeInteger>> MAPPING_WITH_EXCEPTION_HANDLING =
           ExceptionUtils.lift(F);

    @Test
    void givenAMappingFunctionWithExceptionHandling_whenAllTheValuesOfAStreamCanBeSuccessfullyMapped_thenTheNewStreamShouldContainTheMappedValues()
    throws Exception {

        final List<Integer> expected = List.of(0, 1, Integer.MAX_VALUE);

        final Iterable<NonNegativeInteger> result = ExceptionUtils.mapToList(expected.stream(), MAPPING_WITH_EXCEPTION_HANDLING);

        final List<Integer> actual = StreamSupport.stream(result.spliterator(), false)
                .map(NonNegativeInteger::value)
                .collect(Collectors.toList());

        assertEquals(expected, actual);

    }

    @Test
    void givenAMappingFunctionWithExceptionHandling_whenSomeOfTheValuesOfAStreamCannotBeSuccessfullyMapped_thenTheMappingShouldThrowAnException() {

        final List<Integer> expected = List.of(0, 1, -1);

        assertThrows(BGMException.class, () -> ExceptionUtils.mapToList(expected.stream(), MAPPING_WITH_EXCEPTION_HANDLING));

    }

}
