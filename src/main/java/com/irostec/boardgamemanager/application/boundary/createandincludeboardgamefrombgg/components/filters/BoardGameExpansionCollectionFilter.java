package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameExpansion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameExpansionRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameExpansionCollectionFilter
    implements CollectionFilter<Long, Long, BoardGameExpansion, BoundaryException>
{

    private final BoardGameExpansionRepository boardGameExpansionRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameExpansion> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameExpansionRepository.findByExpandedBoardGameIdAndExpanderBoardGameIdIn(sharedKey, uniqueKeys);

    }

}
