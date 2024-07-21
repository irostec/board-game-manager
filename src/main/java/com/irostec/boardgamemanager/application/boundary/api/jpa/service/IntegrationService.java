package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameIntegration;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameIntegrationRepository;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.irostec.boardgamemanager.common.utility.Functions.mapAndProcess;

@Component
@AllArgsConstructor
public class IntegrationService {

    private final BoardGameService boardGameService;
    private final BoardGameIntegrationRepository boardGameIntegrationRepository;

    public <E> Either<E, Collection<BoardGameIntegration>> saveBoardGameIntegrations(
        long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, Collection<BoardGameReference>> boardGameReferencesContainer =
            boardGameService.saveBoardGameReferences(
                    dataSourceId,
                    linksSupplier.get(),
                    linkToKey,
                    exceptionToError
            );

        Either<E, Collection<BoardGameIntegration>> boardGameIntegrationsContainer =
            boardGameReferencesContainer.flatMap(boardGameReferences ->
                mapAndProcess(
                    boardGameReferences,
                    boardGameReference -> {

                        BoardGameIntegration boardGameIntegration = new BoardGameIntegration();
                        boardGameIntegration.setIntegratedBoardGameId(boardGameId);
                        boardGameIntegration.setIntegratingBoardGameId(boardGameReference.getBoardGameId());

                        return boardGameIntegration;
                    },
                    boardGameIntegrationRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameIntegrationsContainer;

    }

}
