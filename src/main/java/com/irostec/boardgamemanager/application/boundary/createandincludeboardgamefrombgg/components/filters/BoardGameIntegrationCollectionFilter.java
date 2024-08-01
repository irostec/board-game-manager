package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameIntegration;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameIntegrationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameIntegrationCollectionFilter
implements CollectionFilter<BoardGame, BoardGame, BoardGameIntegration>
{

    private final BoardGameIntegrationRepository boardGameIntegrationRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameIntegration> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<BoardGame> uniqueKeys
    ) throws BoundaryException {

        return boardGameIntegrationRepository.findByIntegratedAndIntegratingIn(parent, uniqueKeys);

    }

}
