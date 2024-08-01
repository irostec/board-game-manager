package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameImplementationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameImplementationCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class ImplementationService {

    private final BoardGameService boardGameService;
    private final BoardGameImplementationRepository boardGameImplementationRepository;
    private final BoardGameImplementationCollectionFilter boardGameImplementationCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional
    public Collection<BoardGameImplementation> saveBoardGameImplementations(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<BoardGameReference> boardGameReferences =
            boardGameService.saveBoardGameReferences(dataSource, links);

        EntityCollectionProcessor.Result<BoardGameReference, BoardGameImplementation> filteringResult =
            entityCollectionProcessor.apply(
                boardGameReferences,
                boardGame,
                boardGameImplementationCollectionFilter,
                BoardGameReference::getBoardGame,
                BoardGameImplementation::getImplementing
            );

        BiConsumer<BoardGameReference, BoardGameImplementation> boardGameImplementationConsumer =
            (boardGameReference, boardGameImplementation) -> {
                boardGameImplementation.setImplemented(boardGame);
                boardGameImplementation.setImplementing(boardGameReference.getBoardGame());
            };

        Collection<BoardGameImplementation> newBoardGameImplementations = boardGameImplementationRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    boardGameReference -> {

                        BoardGameImplementation boardGameImplementation = new BoardGameImplementation();
                        boardGameImplementation.setImplemented(boardGame);
                        boardGameImplementation.setImplementing(boardGameReference.getBoardGame());

                        return boardGameImplementation;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(boardGameImplementationConsumer);

        return Stream.concat(newBoardGameImplementations.stream(), filteringResult.getMappings().values().stream()).toList();

    }

}
