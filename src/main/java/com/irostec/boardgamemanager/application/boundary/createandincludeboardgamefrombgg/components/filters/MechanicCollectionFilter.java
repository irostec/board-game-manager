package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Mechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.MechanicRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class MechanicCollectionFilter
    implements CollectionFilter<DataSource, String, Mechanic>
{

    private final MechanicRepository mechanicRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<Mechanic> findByParentAndUniqueKeysIn(
        DataSource parent,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return mechanicRepository.findByDataSourceAndExternalIdIn(parent, uniqueKeys);

    }

}
