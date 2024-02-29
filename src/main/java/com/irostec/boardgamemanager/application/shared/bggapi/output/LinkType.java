package com.irostec.boardgamemanager.application.shared.bggapi.output;

import java.util.Arrays;
import java.util.Optional;

/**
 * LinkType
 * Type of link in boardgamegeek.com
 */
public enum LinkType {
    CATEGORY,
    MECHANIC,
    FAMILY,
    EXPANSION,
    ACCESSORY,
    INTEGRATION,
    IMPLEMENTATION,
    DESIGNER,
    ARTIST,
    PUBLISHER;

    public static Optional<LinkType> fromPrefixedName(String prefixedName) {

        return Arrays.stream(LinkType.values())
                .filter(linkType -> prefixedName.compareToIgnoreCase("boardgame" + linkType.name()) == 0)
                .findFirst();

    }

}
