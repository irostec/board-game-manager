package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import io.vavr.control.Validation;

import java.util.Arrays;

/**
 * NameType
 * Type of name in boardgamegeek.com
 */
public enum NameType {
    PRIMARY, ALTERNATE;

    public static Validation<String, NameType> fromName(String name)  {

        return Arrays.stream(NameType.values())
                .filter(nameType -> nameType.name().compareToIgnoreCase(name) == 0)
                .findFirst()
                .map(Validation::<String, NameType>valid)
                .orElseGet(() ->
                        Validation.invalid(
                                String.format("Unknown NameType description: '%s'", name)
                        )
                );

    }

}
