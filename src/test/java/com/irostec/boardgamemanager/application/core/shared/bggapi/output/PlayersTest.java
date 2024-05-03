package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Players;
import com.irostec.boardgamemanager.common.type.PositiveInteger;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlayersTest
 * Verifies that Players instances behave as expected
 */
class PlayersTest {

    private static final String MINIMUM_PLAYERS_PROPERTY_NAME = "minimumPlayers";
    private static final String MAXIMUM_PLAYERS_PROPERTY_NAME = "maximumPlayers";

    @Test
    void givenAPlayers_whenItIsInitializedWithMoreMinimumPlayersThanMaximumPlayers_thenTheValidationShouldFail() {

        final PositiveInteger minimumPlayers = PositiveInteger.of(MINIMUM_PLAYERS_PROPERTY_NAME, 5).get();
        final PositiveInteger maximumPlayers = PositiveInteger.of(MAXIMUM_PLAYERS_PROPERTY_NAME, 4).get();

        final Validation<String, Players> result = Players.of(minimumPlayers, maximumPlayers);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAPlayers_whenItIsInitializedWithEqualMinimumPlayersAndMaximumPlayers_thenTheValidationShouldSucceed() {

        final PositiveInteger minimumPlayers = PositiveInteger.of(MINIMUM_PLAYERS_PROPERTY_NAME, 5).get();

        final Validation<String, Players> result = Players.of(minimumPlayers, minimumPlayers);

        assertTrue(result.isValid());
        assertEquals(minimumPlayers, result.get().getMinimum());
        assertEquals(minimumPlayers, result.get().getMaximum());

    }

    @Test
    void givenAPlayers_whenItIsInitializedWithMoreMaximumPlayersThanMinimumPlayers_thenTheValidationShouldSucceed() {

        final PositiveInteger minimumPlayers = PositiveInteger.of(MINIMUM_PLAYERS_PROPERTY_NAME, 5).get();
        final PositiveInteger maximumPlayers = PositiveInteger.of(MAXIMUM_PLAYERS_PROPERTY_NAME, 6).get();

        final Validation<String, Players> result = Players.of(minimumPlayers, maximumPlayers);

        assertTrue(result.isValid());
        assertEquals(minimumPlayers, result.get().getMinimum());
        assertEquals(maximumPlayers, result.get().getMaximum());

    }

}
