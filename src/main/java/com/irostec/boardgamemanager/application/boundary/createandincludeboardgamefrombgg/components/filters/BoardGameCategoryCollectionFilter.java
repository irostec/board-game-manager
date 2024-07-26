package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameCategory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameCategoryRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameCategoryCollectionFilter
    implements CollectionFilter<Long, Long, BoardGameCategory, BoundaryException>
{

    private final BoardGameCategoryRepository boardGameCategoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameCategory> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameCategoryRepository.findByBoardGameIdAndCategoryIdIn(sharedKey, uniqueKeys);

    }

}
