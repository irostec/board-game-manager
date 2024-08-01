package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameExpansionRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameExpansionCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;

import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class ExpansionService {

    private final BoardGameService boardGameService;
    private final BoardGameExpansionRepository boardGameExpansionRepository;
    private final BoardGameExpansionCollectionFilter boardGameExpansionCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional
    public Collection<BoardGameExpansion> saveBoardGameExpansions(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<BoardGameReference> boardGameReferences =
            boardGameService.saveBoardGameReferences(dataSource, links);

        EntityCollectionProcessor.Result<BoardGameReference, BoardGameExpansion> filteringResult =
            entityCollectionProcessor.apply(
                boardGameReferences,
                boardGame,
                boardGameExpansionCollectionFilter,
                BoardGameReference::getBoardGame,
                BoardGameExpansion::getExpander
            );

        BiConsumer<BoardGameReference, BoardGameExpansion> boardGameExpansionConsumer =
            (boardGameReference, boardGameExpansion) -> {
                boardGameExpansion.setExpanded(boardGame);
                boardGameExpansion.setExpander(boardGameReference.getBoardGame());
            };

        Collection<BoardGameExpansion> newBoardGameExpansions = boardGameExpansionRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    boardGameReference -> {

                        BoardGameExpansion newBoardGameExpansion = new BoardGameExpansion();
                        boardGameExpansionConsumer.accept(boardGameReference, newBoardGameExpansion);

                        return newBoardGameExpansion;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(boardGameExpansionConsumer);

        return Stream.concat(newBoardGameExpansions.stream(), filteringResult.getMappings().values().stream()).toList();

    }

}
