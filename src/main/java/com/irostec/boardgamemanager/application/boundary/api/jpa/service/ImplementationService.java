package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameImplementation;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameImplementationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameImplementationCollectionFilter;
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
public class ImplementationService {

    private final BoardGameService boardGameService;
    private final BoardGameImplementationRepository boardGameImplementationRepository;
    private final BoardGameImplementationCollectionFilter boardGameImplementationCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameImplementation> saveBoardGameImplementations(
        long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<BoardGameReference> boardGameReferences =
            boardGameService.saveBoardGameReferences(dataSourceId, links);

        EntityCollectionProcessor.PartialFunctionDefinition<BoardGameReference, BoardGameImplementation> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                boardGameReferences,
                dataSourceId,
                boardGameImplementationCollectionFilter,
                BoardGameReference::getBoardGameId,
                BoardGameImplementation::getImplementerBoardGameId
            );

        Collection<BoardGameImplementation> newBoardGameImplementations = wrapWithErrorHandling(() ->
            boardGameImplementationRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        boardGameReference -> {

                            BoardGameImplementation boardGameImplementation = new BoardGameImplementation();
                            boardGameImplementation.setImplementedBoardGameId(boardGameId);
                            boardGameImplementation.setImplementerBoardGameId(boardGameReference.getBoardGameId());

                            return boardGameImplementation;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameImplementation> existingBoardGameImplementations = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameImplementations.stream(), existingBoardGameImplementations.stream()).toList();

    }

}
