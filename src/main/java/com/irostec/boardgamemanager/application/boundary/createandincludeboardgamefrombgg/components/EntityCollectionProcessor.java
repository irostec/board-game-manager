package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EntityCollectionProcessor {

    @Transactional(readOnly = true)
    public <I, P, K, O> Result<I, O> apply(
        Collection<I> inputs,
        P parent,
        CollectionFilter<P, K, O> collectionFilter,
        Function<I, K> inputToUniqueKey,
        Function<O, K> outputToUniqueKey
    ) throws BoundaryException {

        Map<K, O> outputsByUniqueKey =
            collectionFilter.findByParentAndUniqueKeysIn(
                parent,
                inputs.stream().map(inputToUniqueKey).toList()
            )
            .collect(Collectors.toMap(outputToUniqueKey, Function.identity()));

        Map<Boolean, List<I>> inputsPartitionedByStatus = inputs.stream()
            .collect(
                Collectors.partitioningBy(
                    input -> outputsByUniqueKey.containsKey(inputToUniqueKey.apply(input))
                )
            );

        List<I> unmapped = inputsPartitionedByStatus.get(false);

        Map<I, O> mappings = inputsPartitionedByStatus.get(true).stream()
            .collect(
                Collectors.toMap(Function.identity(), input -> outputsByUniqueKey.get(inputToUniqueKey.apply(input)))
            );

        return new Result<>(unmapped, mappings);

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static final class Result<I, O> {
        private final List<I> unmapped;
        private final Map<I, O> mappings;
    }

}