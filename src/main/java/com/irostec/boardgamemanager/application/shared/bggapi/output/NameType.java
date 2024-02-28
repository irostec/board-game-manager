package com.irostec.boardgamemanager.application.shared.bggapi.output;

import com.irostec.boardgamemanager.common.exception.BGMException;
import java.util.Arrays;

/**
 * NameType
 * Type of name in boardgamegeek.com
 */
public enum NameType {
    PRIMARY, ALTERNATE;

    public static NameType of(String nameTypeDescription) throws BGMException {

        return Arrays.stream(NameType.values())
                .filter(nameType -> nameType.name().compareToIgnoreCase(nameTypeDescription) == 0)
                .findFirst()
                .orElseThrow(() -> new BGMException(String.format("Unknown NameType description: '%s'", nameTypeDescription)));

    }

}
