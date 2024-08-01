package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Accessory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameAccessory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameAccessoryRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameAccessoryCollectionFilter
implements CollectionFilter<BoardGame, Accessory, BoardGameAccessory>
{

    private final BoardGameAccessoryRepository boardGameAccessoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameAccessory> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Accessory> uniqueKeys
    ) {

        return boardGameAccessoryRepository.findByBoardGameAndAccessoryIn(parent, uniqueKeys);

    }

}
