package com.irostec.boardgamemanager.common.utility;

import io.vavr.CheckedFunction0;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.function.Function;

public final class Functions {

    private Functions() {}

    public static <E, T> Either<E, T> wrapWithFunctionalErrorHandling(
        CheckedFunction0<T> supplier,
        Function<Throwable, E> errorConstructor
    ) {

        return Try.of(supplier)
                .toEither()
                .mapLeft(errorConstructor);

    }

}
