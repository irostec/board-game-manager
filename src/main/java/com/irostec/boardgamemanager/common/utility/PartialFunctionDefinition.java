package com.irostec.boardgamemanager.common.utility;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class PartialFunctionDefinition<I, O>  {

    private final List<I> unmappedDomain;
    private final List<I> mappedDomain;
    private final Collection<O> image;

    public static <X, Y, E> Either<E, PartialFunctionDefinition<X, Y>> of(
        Collection<X> inputs,
        Function<Collection<X>, Either<E, Stream<Y>>> mapping,
        Function<X, String> inputToKey,
        Function<Y, String> outputToKey
    ) {

        Either<E, Map<String, Y>> outputsByInputKeyContainer =
            mapping.apply(inputs)
                .map(stream -> stream.collect(Collectors.toMap(outputToKey, Function.identity())));

        return outputsByInputKeyContainer.map(outputsByInputKey -> {

            Map<Boolean, List<X>> inputsPartitionedByStatus = inputs.stream()
                .collect(
                    Collectors.partitioningBy(
                        input -> outputsByInputKey.keySet().contains(inputToKey.apply(input))
                    )
                );


            List<X> unmappedDomain = inputsPartitionedByStatus.get(false);
            List<X> mappedDomain = inputsPartitionedByStatus.get(true);
            Collection<Y> image = outputsByInputKey.values();

            return new PartialFunctionDefinition(
                unmappedDomain,
                mappedDomain,
                image
            );

        });

    }

}
