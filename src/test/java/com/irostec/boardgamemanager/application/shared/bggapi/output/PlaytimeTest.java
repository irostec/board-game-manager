package com.irostec.boardgamemanager.application.shared.bggapi.output;

import com.irostec.boardgamemanager.common.type.PositiveInteger;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * PlaytimeTest
 * Verifies that Playtime instances behave as expected
 */
class PlaytimeTest {

    private static final String MINIMUM_PLAYTIME_PROPERTY_NAME = "minimumPlaytime";
    private static final String MAXIMUM_PLAYTIME_PROPERTY_NAME = "maximumPlaytime";
    private static final String AVERAGE_PLAYTIME_PROPERTY_NAME = "averagePlaytime";

    @Test
    void givenAPlaytime_whenItIsInitializedWithMoreMinimumPlaytimeThanMaximumPlaytime_thenTheValidationShouldFail() {

        final PositiveInteger minimumPlaytime = PositiveInteger.of(MINIMUM_PLAYTIME_PROPERTY_NAME, 6).get();
        final PositiveInteger maximumPlaytime = PositiveInteger.of(MAXIMUM_PLAYTIME_PROPERTY_NAME, 4).get();
        final PositiveInteger averagePlaytime = PositiveInteger.of(AVERAGE_PLAYTIME_PROPERTY_NAME, 5).get();

        final Validation<String, Playtime> result = Playtime.of(minimumPlaytime, maximumPlaytime, averagePlaytime);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAPlaytime_whenItIsInitializedWithLessAveragePlaytimeThanMinimumPlaytime_thenTheValidationShouldFail() {

        final PositiveInteger minimumPlaytime = PositiveInteger.of(MINIMUM_PLAYTIME_PROPERTY_NAME, 4).get();
        final PositiveInteger maximumPlaytime = PositiveInteger.of(MAXIMUM_PLAYTIME_PROPERTY_NAME, 6).get();
        final PositiveInteger averagePlaytime = PositiveInteger.of(AVERAGE_PLAYTIME_PROPERTY_NAME, 3).get();

        final Validation<String, Playtime> result = Playtime.of(minimumPlaytime, maximumPlaytime, averagePlaytime);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAPlaytime_whenItIsInitializedWithMoreAveragePlaytimeThanMaximumPlaytime_thenTheValidationShouldFail() {

        final PositiveInteger minimumPlaytime = PositiveInteger.of(MINIMUM_PLAYTIME_PROPERTY_NAME, 4).get();
        final PositiveInteger maximumPlaytime = PositiveInteger.of(MAXIMUM_PLAYTIME_PROPERTY_NAME, 6).get();
        final PositiveInteger averagePlaytime = PositiveInteger.of(AVERAGE_PLAYTIME_PROPERTY_NAME, 7).get();

        final Validation<String, Playtime> result = Playtime.of(minimumPlaytime, maximumPlaytime, averagePlaytime);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAPlaytime_whenItIsInitializedWithValidInputs_thenTheValidationShouldSucceed() {

        final PositiveInteger minimumPlaytime = PositiveInteger.of(MINIMUM_PLAYTIME_PROPERTY_NAME, 4).get();
        final PositiveInteger maximumPlaytime = PositiveInteger.of(MAXIMUM_PLAYTIME_PROPERTY_NAME, 6).get();
        final PositiveInteger averagePlaytime = PositiveInteger.of(AVERAGE_PLAYTIME_PROPERTY_NAME, 5).get();

        final Validation<String, Playtime> result = Playtime.of(minimumPlaytime, maximumPlaytime, averagePlaytime);

        assertTrue(result.isValid());
        assertEquals(minimumPlaytime, result.get().getMinimum());
        assertEquals(maximumPlaytime, result.get().getMaximum());
        assertEquals(averagePlaytime, result.get().getAverage());

    }

}
