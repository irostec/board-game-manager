package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.PublisherReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.PublisherReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class PublisherReferenceCollectionFilter
    implements CollectionFilter<Long, String, PublisherReference, BoundaryException>
{

    private final PublisherReferenceRepository publisherReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<PublisherReference> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return publisherReferenceRepository.findByDataSourceIdAndExternalIdIn(sharedKey, uniqueKeys);

    }

}
