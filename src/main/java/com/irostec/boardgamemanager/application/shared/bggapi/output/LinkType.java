package com.irostec.boardgamemanager.application.shared.bggapi.output;

import com.irostec.boardgamemanager.common.exception.BGMException;

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

    public static LinkType of(String linkTypeDescription) throws BGMException {

        return Arrays.stream(LinkType.values())
                .filter(linkType -> linkTypeDescription.compareToIgnoreCase("boardgame" + linkType.name()) == 0)
                .findFirst()
                .orElseThrow(() -> new BGMException(String.format("Unknown LinkType description: '%s'", linkTypeDescription)));

    }

}
