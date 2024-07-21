package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameImplementation;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameImplementationRepository;
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
public class ImplementationService {

    private final BoardGameService boardGameService;
    private final BoardGameImplementationRepository boardGameImplementationRepository;

    public <E> Either<E, Collection<BoardGameImplementation>> saveBoardGameImplementations(
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

        Either<E, Collection<BoardGameImplementation>> boardGameImplementationsContainer =
            boardGameReferencesContainer.flatMap(boardGameReferences ->
                mapAndProcess(
                    boardGameReferences,
                    boardGameReference -> {

                        BoardGameImplementation boardGameImplementation = new BoardGameImplementation();
                        boardGameImplementation.setImplementedBoardGameId(boardGameId);
                        boardGameImplementation.setImplementerBoardGameId(boardGameReference.getBoardGameId());

                        return boardGameImplementation;

                    },
                    boardGameImplementationRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameImplementationsContainer;

    }

}
