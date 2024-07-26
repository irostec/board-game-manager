package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EntityCollectionProcessor {

    public <I, S, U, V> PartialFunctionDefinition<I, V> buildPartialFunctionDefinition(
        Collection<I> inputs,
        S sharedKey,
        CollectionFilter<S, U, V, BoundaryException> collectionFilter,
        Function<I, U> inputToUniqueKey,
        Function<V, U> outputToUniqueKey
    ) throws BoundaryException {

        Map<U, V> outputsByUniqueKey =
            collectionFilter.findBySharedKeyAndUniqueKeysIn(
                sharedKey,
                inputs.stream().map(inputToUniqueKey).toList()
            )
            .collect(Collectors.toMap(outputToUniqueKey, Function.identity()));

        Map<Boolean, List<I>> inputsPartitionedByStatus = inputs.stream()
            .collect(
                Collectors.partitioningBy(
                    input -> outputsByUniqueKey.containsKey(inputToUniqueKey.apply(input))
                )
            );

        List<I> unmappedDomain = inputsPartitionedByStatus.get(false);
        List<I> mappedDomain = inputsPartitionedByStatus.get(true);
        Collection<V> image = outputsByUniqueKey.values();

        return new PartialFunctionDefinition<>(unmappedDomain, mappedDomain, image);

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static final class PartialFunctionDefinition<I, O> {

        private final List<I> unmappedDomain;
        private final List<I> mappedDomain;
        private final Collection<O> image;

    }

}