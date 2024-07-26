package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGamePublisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGamePublisherRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGamePublisherCollectionFilter
    implements CollectionFilter<Long, Long, BoardGamePublisher, BoundaryException>
{

    private final BoardGamePublisherRepository boardGameDesignerRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGamePublisher> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameDesignerRepository.findByBoardGameIdAndPublisherIdIn(sharedKey, uniqueKeys);

    }

}
