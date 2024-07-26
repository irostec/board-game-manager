package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameExpansion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameExpansionRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameExpansionCollectionFilter;
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
public class ExpansionService {

    private final BoardGameService boardGameService;
    private final BoardGameExpansionRepository boardGameExpansionRepository;
    private final BoardGameExpansionCollectionFilter boardGameExpansionCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameExpansion> saveBoardGameExpansions(
        long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<BoardGameReference> boardGameReferences =
            boardGameService.saveBoardGameReferences(dataSourceId, links);

        EntityCollectionProcessor.PartialFunctionDefinition<BoardGameReference, BoardGameExpansion> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                boardGameReferences,
                dataSourceId,
                boardGameExpansionCollectionFilter,
                BoardGameReference::getBoardGameId,
                BoardGameExpansion::getExpanderBoardGameId
            );

        Collection<BoardGameExpansion> newBoardGameExpansions = wrapWithErrorHandling(() ->
            boardGameExpansionRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                .map(
                    boardGameReference -> {

                        BoardGameExpansion boardGameExpansion = new BoardGameExpansion();
                        boardGameExpansion.setExpandedBoardGameId(boardGameId);
                        boardGameExpansion.setExpanderBoardGameId(boardGameReference.getBoardGameId());

                        return boardGameExpansion;
                    }
                )
                .toList()
            )
        );

        Collection<BoardGameExpansion> existingBoardGameExpansions = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameExpansions.stream(), existingBoardGameExpansions.stream()).toList();

    }

}
