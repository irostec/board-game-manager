package com.irostec.boardgamemanager.application.shared.bggapi.output;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

/**
 * NameTypeTest
 * Validate that getting a NameType from a string value works correctly
 */
class NameTypeTest {

    @Test
    void buildNameTypeFromValidName() {

        final String nameOfValidNameType = "alternate";

        final Optional<NameType> result = NameType.fromName(nameOfValidNameType);

        assertFalse(result.isEmpty());
        assertEquals(NameType.ALTERNATE, result.get());

    }

    @Test
    void buildNameTypeFromInvalidName() {

        final String nameOfInvalidNameType = "unknown";

        final Optional<NameType> result = NameType.fromName(nameOfInvalidNameType);

        assertTrue(result.isEmpty());

    }

}
