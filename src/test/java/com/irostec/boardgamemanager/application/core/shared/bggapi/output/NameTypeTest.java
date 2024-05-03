package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.NameType;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NameTypeTest
 * Validate that getting a NameType from a string value works correctly
 */
class NameTypeTest {

    @Test
    void buildNameTypeFromValidName() {

        final String nameOfValidNameType = "alternate";

        final Validation<String, NameType> result = NameType.fromName(nameOfValidNameType);

        assertTrue(result.isValid());
        assertEquals(NameType.ALTERNATE, result.get());

    }

    @Test
    void buildNameTypeFromInvalidName() {

        final String nameOfInvalidNameType = "unknown";

        final Validation<String, NameType> result = NameType.fromName(nameOfInvalidNameType);

        assertTrue(result.isInvalid());

    }

}
