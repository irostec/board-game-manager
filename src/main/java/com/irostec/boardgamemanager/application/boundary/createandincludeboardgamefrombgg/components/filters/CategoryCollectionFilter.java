package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.CategoryRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class CategoryCollectionFilter
    implements CollectionFilter<DataSource, String, Category>
{

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<Category> findByParentAndUniqueKeysIn(
        DataSource parent,
        Collection<String> uniqueKeys
    ) {

        return categoryRepository.findByDataSourceAndExternalIdIn(parent, uniqueKeys);

    }

}
