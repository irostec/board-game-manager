package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFamily;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameFamilyRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameFamilyCollectionFilter
    implements CollectionFilter<Long, Long, BoardGameFamily, BoundaryException>
{

    private final BoardGameFamilyRepository boardGameFamilyRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameFamily> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameFamilyRepository.findByBoardGameIdAndFamilyIdIn(sharedKey, uniqueKeys);

    }

}
