package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameImplementation;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameImplementationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameImplementationCollectionFilter
    implements CollectionFilter<BoardGame, BoardGame, BoardGameImplementation>
{

    private final BoardGameImplementationRepository boardGameImplementationRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameImplementation> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<BoardGame> uniqueKeys
    ) {

        return boardGameImplementationRepository.findByImplementedAndImplementingIn(parent, uniqueKeys);

    }

}
