package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.AccessoryReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.AccessoryReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@AllArgsConstructor
@Component
public class AccessoryReferenceCollectionFilter
    implements CollectionFilter<Long, String, AccessoryReference, BoundaryException>
{

    private final AccessoryReferenceRepository accessoryReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<AccessoryReference> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return wrapWithErrorHandling(
            () -> accessoryReferenceRepository.findByDataSourceIdAndExternalIdIn(sharedKey, uniqueKeys)
        );

    }

}
