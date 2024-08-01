package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DesignerReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DesignerReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class DesignerReferenceCollectionFilter
    implements CollectionFilter<DataSource, String, DesignerReference>
{

    private final DesignerReferenceRepository designerReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<DesignerReference> findByParentAndUniqueKeysIn(
        DataSource parent,
        Collection<String> uniqueKeys
    ) {

        return designerReferenceRepository.findByDataSourceAndExternalIdIn(parent, uniqueKeys);

    }

}
