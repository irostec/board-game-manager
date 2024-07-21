package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameExpansion;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameExpansionRepository;
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
public class ExpansionService {

    private final BoardGameService boardGameService;
    private final BoardGameExpansionRepository boardGameExpansionRepository;

    public <E> Either<E, Collection<BoardGameExpansion>> saveBoardGameExpansions(
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

        Either<E, Collection<BoardGameExpansion>> boardGameExpansionsContainer =
            boardGameReferencesContainer.flatMap(boardGameReferences ->
                mapAndProcess(
                    boardGameReferences,
                    boardGameReference -> {

                        BoardGameExpansion boardGameExpansion = new BoardGameExpansion();
                        boardGameExpansion.setExpandedBoardGameId(boardGameId);
                        boardGameExpansion.setExpanderBoardGameId(boardGameReference.getBoardGameId());

                        return boardGameExpansion;
                    },
                    boardGameExpansionRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameExpansionsContainer;

    }

}
