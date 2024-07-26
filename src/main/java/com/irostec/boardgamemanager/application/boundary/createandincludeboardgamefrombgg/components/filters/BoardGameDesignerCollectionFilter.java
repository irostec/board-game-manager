package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameDesigner;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameDesignerRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameDesignerCollectionFilter
    implements CollectionFilter<Long, Long, BoardGameDesigner, BoundaryException>
{

    private final BoardGameDesignerRepository boardGameDesignerRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameDesigner> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameDesignerRepository.findByBoardGameIdAndDesignerIdIn(sharedKey, uniqueKeys);

    }

}
