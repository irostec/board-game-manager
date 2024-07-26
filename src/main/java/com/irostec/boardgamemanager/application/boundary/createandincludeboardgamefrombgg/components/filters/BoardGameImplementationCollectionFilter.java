package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameImplementation;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameImplementationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameImplementationCollectionFilter
    implements CollectionFilter<Long, Long, BoardGameImplementation, BoundaryException>
{

    private final BoardGameImplementationRepository boardGameImplementationRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameImplementation> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameImplementationRepository.findByImplementedBoardGameIdAndImplementerBoardGameIdIn(sharedKey, uniqueKeys);

    }

}
