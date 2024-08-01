package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameMechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Mechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameMechanicRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameMechanicCollectionFilter
    implements CollectionFilter<BoardGame, Mechanic, BoardGameMechanic>
{

    private final BoardGameMechanicRepository boardGameMechanicRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameMechanic> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Mechanic> uniqueKeys
    ) throws BoundaryException {

        return boardGameMechanicRepository.findByBoardGameAndMechanicIn(parent, uniqueKeys);

    }

}
