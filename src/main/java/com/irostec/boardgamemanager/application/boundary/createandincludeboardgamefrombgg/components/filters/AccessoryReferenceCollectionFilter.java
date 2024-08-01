package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.AccessoryReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.AccessoryReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class AccessoryReferenceCollectionFilter
    implements CollectionFilter<DataSource, String, AccessoryReference>
{

    private final AccessoryReferenceRepository accessoryReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<AccessoryReference> findByParentAndUniqueKeysIn(
        DataSource parent,
        Collection<String> uniqueKeys
    ) {

      return accessoryReferenceRepository.findByDataSourceAndExternalIdIn(parent, uniqueKeys);

    }

}
