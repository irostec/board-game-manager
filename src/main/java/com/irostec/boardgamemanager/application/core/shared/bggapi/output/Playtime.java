package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import com.irostec.boardgamemanager.common.type.PositiveShort;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Playtime
 * Contains information about the time required to play a game
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Playtime {

    private final PositiveShort minimum;
    private final PositiveShort maximum;
    private final PositiveShort average;

    public static Validation<String, Playtime> of(PositiveShort minimum,
                                                  PositiveShort maximum,
                                                  PositiveShort average) {

        return minimum.getValue() > maximum.getValue() ?
                Validation.invalid(
                        String.format("Minimum playtime (%d) should be less than or equal to maximum playtime (%d)", minimum.getValue(), maximum.getValue())
                ) :
                average.getValue() < minimum.getValue() || average.getValue() > maximum.getValue() ?
                        Validation.invalid(
                                String.format("The average playtime should between %d and %d.", minimum.getValue(), maximum.getValue())
                        ) :
                        Validation.valid(new Playtime(minimum, maximum, average));

    }

}
