package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameIntegrationRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameIntegrationCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class IntegrationService {

    private final BoardGameService boardGameService;
    private final BoardGameIntegrationRepository boardGameIntegrationRepository;
    private final BoardGameIntegrationCollectionFilter boardGameIntegrationCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional
    public Collection<BoardGameIntegration> saveBoardGameIntegrations(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<BoardGameReference> boardGameReferences =
            boardGameService.saveBoardGameReferences(dataSource, links);

        EntityCollectionProcessor.Result<BoardGameReference, BoardGameIntegration> filteringResult =
            entityCollectionProcessor.apply(
                boardGameReferences,
                boardGame,
                boardGameIntegrationCollectionFilter,
                BoardGameReference::getBoardGame,
                BoardGameIntegration::getIntegrating
            );

        BiConsumer<BoardGameReference, BoardGameIntegration> boardGameIntegrationConsumer =
            (boardGameReference, boardGameIntegration) -> {
                boardGameIntegration.setIntegrated(boardGame);
                boardGameIntegration.setIntegrating(boardGameReference.getBoardGame());
            };

        Collection<BoardGameIntegration> newBoardGameIntegrations = boardGameIntegrationRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    boardGameReference -> {

                        BoardGameIntegration newBoardGameIntegration = new BoardGameIntegration();
                        boardGameIntegrationConsumer.accept(boardGameReference, newBoardGameIntegration);

                        return newBoardGameIntegration;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(boardGameIntegrationConsumer);

        return Stream.concat(newBoardGameIntegrations.stream(), filteringResult.getMappings().values().stream()).toList();

    }

}
