package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import com.irostec.boardgamemanager.common.type.PositiveInteger;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Players
 * Contains information about the number of players for a board game
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Players {

    private final PositiveInteger minimum;
    private final PositiveInteger maximum;

    public static Validation<String, Players> of(PositiveInteger minimum, PositiveInteger maximum) {

        return minimum.getValue() > maximum.getValue() ?
                Validation.invalid(
                        String.format("The minimum number of players (%d) should be smaller than or equal to the max number of players (%d).", minimum.getValue(), maximum.getValue())
                ) :
                Validation.valid(new Players(minimum, maximum));

    }

}
