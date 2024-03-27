package com.irostec.boardgamemanager.application.shared.bggapi.output;

import io.vavr.control.Validation;

import java.util.Arrays;

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

    public static Validation<String, LinkType> fromPrefixedName(String prefixedName) {

        return Arrays.stream(LinkType.values())
                .filter(linkType -> prefixedName.compareToIgnoreCase("boardgame" + linkType.name()) == 0)
                .findFirst()
                .map(Validation::<String, LinkType>valid)
                .orElseGet(() ->
                        Validation.invalid(String.format("Unknown LinkType description: '%s'", prefixedName))
                );

    }

}
