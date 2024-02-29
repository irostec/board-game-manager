package com.irostec.boardgamemanager.common.utility;

import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    void testMapToListWithValidInput() throws BGMException {

        testMapToCollection(
                List.of(0, 1, Integer.MAX_VALUE),
                stream -> ExceptionUtils.mapToList(stream, MAPPING_WITH_EXCEPTION_HANDLING),
                Collectors.toList()
        );

    }

    @Test
    void testMapToSetWithValidInput() throws BGMException {

        testMapToCollection(
                Set.of(0, 1, Integer.MAX_VALUE),
                stream -> ExceptionUtils.mapToSet(stream, MAPPING_WITH_EXCEPTION_HANDLING),
                Collectors.toSet()
        );

    }

    @Test
    void testMapToListWithInvalidInput() {

        final List<Integer> expected = List.of(0, 1, -1);

        assertThrows(BGMException.class, () -> ExceptionUtils.mapToList(expected.stream(), MAPPING_WITH_EXCEPTION_HANDLING));

    }

    @Test
    void testMapToSetWithInvalidInput() {

        final Set<Integer> expected = Set.of(0, 1, -1);

        assertThrows(BGMException.class, () -> ExceptionUtils.mapToSet(expected.stream(), MAPPING_WITH_EXCEPTION_HANDLING));

    }

    private static <C1 extends Collection<Integer>, C2 extends Collection<NonNegativeInteger>> void testMapToCollection(
            C1 expectedCollection,
            Checked.Function<Stream<Integer>, C2, BGMException> mapping,
            Collector<Integer, ?, C1> collector
    ) throws BGMException {

        final C2 mappedCollection = mapping.apply(expectedCollection.stream());

        final C1 actualCollection = mappedCollection.stream()
                .map(NonNegativeInteger::value)
                .collect(collector);

        assertEquals(expectedCollection, actualCollection);

    }

}
