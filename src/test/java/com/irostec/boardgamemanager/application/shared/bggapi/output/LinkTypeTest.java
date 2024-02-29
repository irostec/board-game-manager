package com.irostec.boardgamemanager.application.shared.bggapi.output;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

/**
 * LinkTypeTest
 * Validate that getting a LinkType from a string value works correctly
 */
class LinkTypeTest {

    @Test
    void buildLinkTypeFromValidName() {

        final String nameOfValidLinkType = "boardgamecategory";

        final Optional<LinkType> result = LinkType.fromPrefixedName(nameOfValidLinkType);

        assertFalse(result.isEmpty());
        assertEquals(LinkType.CATEGORY, result.get());

    }

    @Test
    void buildLinkTypeFromInvalidName() {

        final String nameOfInvalidLinkType = "unknown";

        final Optional<LinkType> result = LinkType.fromPrefixedName(nameOfInvalidLinkType);

        assertTrue(result.isEmpty());

    }

}
