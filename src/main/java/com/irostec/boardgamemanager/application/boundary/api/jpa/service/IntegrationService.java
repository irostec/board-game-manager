package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameIntegration;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameIntegrationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameIntegrationCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class IntegrationService {

    private final BoardGameService boardGameService;
    private final BoardGameIntegrationRepository boardGameIntegrationRepository;
    private final BoardGameIntegrationCollectionFilter boardGameIntegrationCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameIntegration> saveBoardGameIntegrations(
        long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<BoardGameReference> boardGameReferences =
            boardGameService.saveBoardGameReferences(dataSourceId, links);

        EntityCollectionProcessor.PartialFunctionDefinition<BoardGameReference, BoardGameIntegration> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                boardGameReferences,
                dataSourceId,
                boardGameIntegrationCollectionFilter,
                BoardGameReference::getBoardGameId,
                BoardGameIntegration::getIntegratingBoardGameId
            );

        Collection<BoardGameIntegration> newBoardGameIntegrations = wrapWithErrorHandling(() ->
            boardGameIntegrationRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        boardGameReference -> {

                            BoardGameIntegration boardGameIntegration = new BoardGameIntegration();
                            boardGameIntegration.setIntegratedBoardGameId(boardGameId);
                            boardGameIntegration.setIntegratingBoardGameId(boardGameReference.getBoardGameId());

                            return boardGameIntegration;
                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameIntegration> existingBoardGameIntegrations = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameIntegrations.stream(), existingBoardGameIntegrations.stream()).toList();

    }

}
