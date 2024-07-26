package com.irostec.boardgamemanager.common.error;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.List;
import java.util.stream.Stream;

/**
 * ErrorUtils
 * Utility methods to make error handling easier
 */
public final class ErrorUtils {

    private ErrorUtils() {}

    public static <T> Validation<List<String>, List<T>> sequence(
            Stream<Validation<String, T>> validations
    ) {

        return Validation.sequence(
                validations
                        .map(validation -> validation.mapError(io.vavr.collection.List::of))
                        .toList()
        )
        .mapError(Seq::asJava)
        .map(Seq::asJava);

    }

}
