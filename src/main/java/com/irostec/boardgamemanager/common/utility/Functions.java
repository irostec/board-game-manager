package com.irostec.boardgamemanager.common.utility;

import io.vavr.CheckedFunction0;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

public final class Functions {

    private Functions() {}

    public static <I1, I2, O> BiFunction<Collection<I1>, Collection<I2>, Collection<O>> zipper(
        BiFunction<I1, I2, O> f
    ) {

        return (collection1, collection2) ->
            Streams.zip(collection1.stream(), collection2.stream(), f)
                .collect(Collectors.toList());

    }

    public static <I1, I2, M1, M2, O> BiFunction<Collection<I1>, Collection<I2>, Collection<O>> orderedZipper(
        Function<I1, String> input1ToKey,
        Function<I2, String> input2ToKey,
        Function<I1, M1> mapInput1,
        Function<I2, M2> mapInput2,
        BiFunction<M1, M2, O> f
    ) {

        return (collection1, collection2) ->
            Streams.zip(
                collection1.stream()
                .sorted(Comparator.comparing(input1ToKey))
                .map(mapInput1),
                collection2.stream()
                .sorted(Comparator.comparing(input2ToKey))
                .map(mapInput2),
                f
            )
            .collect(Collectors.toList());

    }

    public static <I, T, O, E> Either<E, Collection<O>> mapAndProcess(
        Collection<I> inputs,
        Function<I, T> inputToItem,
        Function<Collection<T>, Collection<O>> processItems,
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            () ->
                processItems.apply(
                    inputs.stream()
                        .map(inputToItem)
                        .collect(Collectors.toList())
                ),
                exceptionToError
        );

    }

    public static <T, E> Either<E, T> wrapWithErrorHandling(
        CheckedFunction0<T> supplier,
        Function<Throwable, E> errorConstructor
    ) {

        return Try.of(supplier)
            .toEither()
            .mapLeft(errorConstructor);

    }

    public static <I, O, E, A, B, C, T> Either<E, T> processItems(
        PartialFunctionDefinition<I, A> partialFunctionDefinition,
        Function<Collection<I>, Either<E, Collection<B>>> processNewInputs,
        Function<Collection<A>, Either<E, Collection<C>>> processExistingInputs,
        BiFunction<Collection<I>, Collection<B>, Collection<O>> joinNewItemsWithInputs,
        BiFunction<Collection<I>, Collection<C>, Collection<O>> joinExistingItemsWithInputs,
        BiFunction<Collection<O>, Collection<O>, T> buildResult
    ) {

        List<I> newInputs = partialFunctionDefinition.getUnmappedDomain();
        List<I> existingInputs = partialFunctionDefinition.getMappedDomain();
        Collection<A> matchingValues = partialFunctionDefinition.getImage();

        Either<E, Collection<B>> processedNewItemsContainer = processNewInputs.apply(newInputs);
        Either<E, Collection<C>> existingItemsContainer = processExistingInputs.apply(matchingValues);

        Either<E, Collection<O>> newItemsWithInputsContainer = processedNewItemsContainer.map(
            processedNewItems -> joinNewItemsWithInputs.apply(newInputs, processedNewItems)
        );

        Either<E, Collection<O>> existingItemsWithInputsContainer = existingItemsContainer.map(
            existingItems -> joinExistingItemsWithInputs.apply(existingInputs, existingItems)
        );

        return newItemsWithInputsContainer.flatMap(
            newItemsWithInputs -> existingItemsWithInputsContainer.map(
                existingItemsWithInputs -> buildResult.apply(newItemsWithInputs, existingItemsWithInputs)
            )
        );

    }

    public static <I, O, E, T> Either<E, T> processItems(
        Map<Boolean, Collection<I>> inputs,
        Function<Collection<I>, Either<E, Collection<O>>> processNewInputs,
        Function<Collection<I>, Either<E, Collection<O>>> processExistingInputs,
        BiFunction<Collection<O>, Collection<O>, T> buildResult
    ) {

        Collection<I> newInputs = inputs.get(true);
        Collection<I> existingInputs = inputs.get(false);

        Either<E, Collection<O>> newItemsContainer = processNewInputs.apply(newInputs);
        Either<E, Collection<O>> existingItemsContainer = processExistingInputs.apply(existingInputs);

        Either<E, T> result = newItemsContainer.flatMap(
            newItems -> existingItemsContainer.map(
                existingItems -> buildResult.apply(newItems, existingItems)
            )
        );

        return result;

    }

}
