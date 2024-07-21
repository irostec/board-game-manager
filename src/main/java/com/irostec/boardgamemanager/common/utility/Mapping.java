package com.irostec.boardgamemanager.common.utility;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * MappingUtils
 * Useful methods to transform objects into java.util.Map instances
 */
public final class Mapping {

    private Mapping() {}

    public static <T, K, V> Map<K, V> toMap(
            T source,
            ImmutablePair<Function<T, K>, Function<T, V>>... extractors
    ) {

        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        for (ImmutablePair<Function<T, K>, Function<T, V>> extractor : extractors) {
            builder.put(extractor.left.apply(source), extractor.right.apply(source));
        }

        return builder.build();

    }

    public static <T, K, V> List<Map<K, V>> toListOfMaps(
            Collection<T> source,
            ImmutablePair<Function<T, K>, Function<T, V>>... extractors
    ) {

        return source.stream()
                .map(element -> toMap(element, extractors))
                .collect(ImmutableList.toImmutableList());

    }

}
