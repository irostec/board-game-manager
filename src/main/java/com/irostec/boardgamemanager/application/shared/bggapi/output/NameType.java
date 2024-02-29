package com.irostec.boardgamemanager.application.shared.bggapi.output;

import java.util.Arrays;
import java.util.Optional;

/**
 * NameType
 * Type of name in boardgamegeek.com
 */
public enum NameType {
    PRIMARY, ALTERNATE;

    public static Optional<NameType> fromName(String name)  {

        return Arrays.stream(NameType.values())
                .filter(nameType -> nameType.name().compareToIgnoreCase(name) == 0)
                .findFirst();

    }

}
