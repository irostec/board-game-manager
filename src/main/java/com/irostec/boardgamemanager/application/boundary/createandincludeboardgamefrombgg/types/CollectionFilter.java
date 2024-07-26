package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types;

import java.util.Collection;
import java.util.stream.Stream;

public interface CollectionFilter<K1, K2, V, E extends Exception> {

    Stream<V> findBySharedKeyAndUniqueKeysIn(K1 sharedKey, Collection<K2> uniqueKeys) throws E;

}
