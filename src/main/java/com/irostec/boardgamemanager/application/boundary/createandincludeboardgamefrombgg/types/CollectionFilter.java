package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types;

import java.util.Collection;
import java.util.stream.Stream;

public interface CollectionFilter<P, K, V> {

    Stream<V> findByParentAndUniqueKeysIn(P parent, Collection<K> uniqueKeys);

}
