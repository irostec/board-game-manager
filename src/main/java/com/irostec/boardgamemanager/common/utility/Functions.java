package com.irostec.boardgamemanager.common.utility;

import com.irostec.boardgamemanager.common.error.BoundaryException;
import io.vavr.CheckedFunction0;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.function.Function;

public final class Functions {

    private Functions() {}

    private static Function<Throwable, BoundaryException> BOUNDARY_EXCEPTION_CONSTRUCTOR = BoundaryException::new;

    public static <E, T> Either<E, T> wrapWithFunctionalErrorHandling(
        CheckedFunction0<T> supplier,
        Function<Throwable, E> errorConstructor
    ) {

        return Try.of(supplier)
                .toEither()
                .mapLeft(errorConstructor);

    }

    public static <E extends Exception, T> T wrapWithErrorHandling(
        CheckedFunction0<T> supplier,
        Function<Throwable, E> exceptionMapping
    ) throws E {

        try {
            return supplier.apply();
        }
        catch (Throwable ex) {
            throw exceptionMapping.apply(ex);
        }

    }

    public static <T> T wrapWithErrorHandling(CheckedFunction0<T> supplier) throws BoundaryException {
        return wrapWithErrorHandling(supplier, BOUNDARY_EXCEPTION_CONSTRUCTOR);
    }

}
