package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameDesigner;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Designer;
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
    implements CollectionFilter<BoardGame, Designer, BoardGameDesigner>
{

    private final BoardGameDesignerRepository boardGameDesignerRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameDesigner> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Designer> uniqueKeys
    ) throws BoundaryException {

        return boardGameDesignerRepository.findByBoardGameAndDesignerIn(parent, uniqueKeys);

    }

}
