package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ArtistReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ArtistReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;

import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class ArtistReferenceCollectionFilter
    implements CollectionFilter<DataSource, String, ArtistReference>
{

    private final ArtistReferenceRepository artistReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<ArtistReference> findByParentAndUniqueKeysIn(
        DataSource parent,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return artistReferenceRepository.findByDataSourceAndExternalIdIn(parent, uniqueKeys);

    }

}
