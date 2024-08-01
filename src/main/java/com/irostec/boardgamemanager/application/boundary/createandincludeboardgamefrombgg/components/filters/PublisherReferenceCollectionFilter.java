package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
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
    implements CollectionFilter<DataSource, String, PublisherReference>
{

    private final PublisherReferenceRepository publisherReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<PublisherReference> findByParentAndUniqueKeysIn(
        DataSource parent,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return publisherReferenceRepository.findByDataSourceAndExternalIdIn(parent, uniqueKeys);

    }

}
