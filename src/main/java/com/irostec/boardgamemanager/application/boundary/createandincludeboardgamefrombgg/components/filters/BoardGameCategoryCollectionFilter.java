package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameCategory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameCategoryRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameCategoryCollectionFilter
    implements CollectionFilter<BoardGame, Category, BoardGameCategory>
{

    private final BoardGameCategoryRepository boardGameCategoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameCategory> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Category> uniqueKeys
    ) {

        return boardGameCategoryRepository.findByBoardGameAndCategoryIn(parent, uniqueKeys);

    }

}
