package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameExpansion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameExpansionRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameExpansionCollectionFilter
    implements CollectionFilter<BoardGame, BoardGame, BoardGameExpansion>
{

    private final BoardGameExpansionRepository boardGameExpansionRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameExpansion> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<BoardGame> uniqueKeys
    ) {

        return boardGameExpansionRepository.findByExpandedAndExpanderIn(parent, uniqueKeys);

    }

}
