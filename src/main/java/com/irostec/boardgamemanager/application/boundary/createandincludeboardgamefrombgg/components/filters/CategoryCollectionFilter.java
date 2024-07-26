package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.CategoryRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class CategoryCollectionFilter
    implements CollectionFilter<Long, String, Category, BoundaryException>
{

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<Category> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return categoryRepository.findByDataSourceIdAndExternalIdIn(sharedKey, uniqueKeys);

    }

}
