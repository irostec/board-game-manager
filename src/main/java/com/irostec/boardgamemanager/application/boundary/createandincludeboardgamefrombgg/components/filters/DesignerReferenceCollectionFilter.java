package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DesignerReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DesignerReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class DesignerReferenceCollectionFilter
    implements CollectionFilter<Long, String, DesignerReference, BoundaryException>
{

    private final DesignerReferenceRepository designerReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<DesignerReference> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return designerReferenceRepository.findByDataSourceIdAndExternalIdIn(sharedKey, uniqueKeys);

    }

}
