package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Publisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.PublisherReference;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query(
        "SELECT new org.apache.commons.lang3.tuple.ImmutablePair(publisher, publisherReference) " +
        "FROM Publisher publisher " +
        "LEFT JOIN PublisherReference publisherReference ON publisher.id = publisherReference.publisherId " +
        "WHERE publisherReference.dataSourceId = :dataSourceId AND publisherReference.externalId IN :externalIds"
    )
    Stream<ImmutablePair<Publisher, PublisherReference>> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}
