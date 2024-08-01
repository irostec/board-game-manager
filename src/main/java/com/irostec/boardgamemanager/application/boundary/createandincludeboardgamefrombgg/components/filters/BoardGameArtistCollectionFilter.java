package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Artist;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
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
    implements CollectionFilter<BoardGame, Artist, BoardGameArtist>
{

    private final BoardGameArtistRepository boardGameArtistRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameArtist> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Artist> uniqueKeys
    ) throws BoundaryException {

        return boardGameArtistRepository.findByBoardGameAndArtistIn(parent, uniqueKeys);

    }

}
