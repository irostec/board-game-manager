package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFamily;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameFamilyRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameFamilyCollectionFilter
    implements CollectionFilter<BoardGame, Family, BoardGameFamily>
{

    private final BoardGameFamilyRepository boardGameFamilyRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameFamily> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Family> uniqueKeys
    ) {

        return boardGameFamilyRepository.findByBoardGameAndFamilyIn(parent, uniqueKeys);

    }

}
