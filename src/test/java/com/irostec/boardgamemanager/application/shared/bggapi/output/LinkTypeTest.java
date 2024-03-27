package com.irostec.boardgamemanager.application.shared.bggapi.output;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LinkTypeTest
 * Validate that getting a LinkType from a string value works correctly
 */
class LinkTypeTest {

    @Test
    void buildLinkTypeFromValidName() {

        final String nameOfValidLinkType = "boardgamecategory";

        final Validation<String, LinkType> result = LinkType.fromPrefixedName(nameOfValidLinkType);

        assertTrue(result.isValid());
        assertEquals(LinkType.CATEGORY, result.get());

    }

    @Test
    void buildLinkTypeFromInvalidName() {

        final String nameOfInvalidLinkType = "unknown";

        final Validation<String, LinkType> result = LinkType.fromPrefixedName(nameOfInvalidLinkType);

        assertTrue(result.isInvalid());

    }

}
