package com.irostec.boardgamemanager.common.utility;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.irostec.boardgamemanager.common.exception.BGMException;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Eithers;
import io.atlassian.fugue.Try;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * ExceptionUtils
 * In Java, handling functions that throw checked exceptions is usually cumbersome, especially with Streams.
 * The methods in this class make it more convenient.
 */
public final class ExceptionUtils {

    private ExceptionUtils() {}

    private static <A, B, E extends Exception> Iterable<B> map(
        Stream<A> source,
        Function<A, Either<E, B>> mappingWithExceptionHandling
    ) throws E {

        return Eithers.getOrThrow(
                Eithers.sequenceRight(
                        () -> source.map(mappingWithExceptionHandling).iterator()
                )
        );

    }

    /*
    public static <A, B, E extends Exception> Stream<B> mapToStream(
            Stream<A> source,
            Function<A, Either<E, B>> mappingWithExceptionHandling
    ) throws E {

        return StreamSupport.stream(map(source, mappingWithExceptionHandling).spliterator(), false);

    }
    */

    public static <A, B, E extends Exception> List<B> mapToList(
        Stream<A> source,
        Function<A, Either<E, B>> mappingWithExceptionHandling
    ) throws E {

        return ImmutableList.copyOf(map(source, mappingWithExceptionHandling));

    }

    public static <A, B, E extends Exception> Set<B> mapToSet(
            Stream<A> source,
            Function<A, Either<E, B>> mappingWithExceptionHandling
    ) throws E {

        return ImmutableSet.copyOf(map(source, mappingWithExceptionHandling));

    }

    private static <A, B, E1 extends Exception, E2 extends Exception > Function<A, Either<E2, B>> lift(
            Checked.Function<A,B,E1> f,
            Function<Exception, E2> exceptionMapping
    ) {

        return Checked.lift(f).andThen(Try::toEither).andThen(either -> either.leftMap(exceptionMapping));

    }

    public static <A, B, E extends Exception> Function<A, Either<BGMException, B>> lift(Checked.Function<A,B,E> f) {

        return lift(f, BGMException::new);

    }

}
