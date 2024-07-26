package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameArtist;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameArtistRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameArtistCollectionFilter
    implements CollectionFilter<Long, Long, BoardGameArtist, BoundaryException>
{

    private final BoardGameArtistRepository boardGameArtistRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameArtist> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<Long> uniqueKeys
    ) throws BoundaryException {

        return boardGameArtistRepository.findByBoardGameIdAndArtistIdIn(sharedKey, uniqueKeys);

    }

}
