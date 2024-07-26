package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.FamilyRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class FamilyCollectionFilter
    implements CollectionFilter<Long, String, Family, BoundaryException>
{

    private final FamilyRepository familyRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<Family> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return familyRepository.findByDataSourceIdAndExternalIdIn(sharedKey, uniqueKeys);

    }

}
