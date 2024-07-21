package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFixedData;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameFixedDataRepository;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Name;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.NameType;
import com.irostec.boardgamemanager.common.utility.PartialFunctionDefinition;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;
import static com.irostec.boardgamemanager.common.utility.Functions.mapAndProcess;
import static com.irostec.boardgamemanager.common.utility.Functions.processItems;
import static com.irostec.boardgamemanager.common.utility.Functions.orderedZipper;
import static com.irostec.boardgamemanager.common.utility.Functions.zipper;

@Component
@AllArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final BoardGameReferenceRepository boardGameReferenceRepository;
    private final BoardGameFixedDataRepository boardGameFixedDataRepository;

    public <E> Either<E, BoardGameFixedData> saveBoardGameFixedData(
        Long boardGameId,
        BoardGameFromBGG boardGameFromBGG,
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            () -> {

                BoardGameFixedData boardGameFixedData = new BoardGameFixedData();
                boardGameFixedData.setBoardGameId(boardGameId);
                boardGameFixedData.setYearOfPublication(boardGameFromBGG.yearPublished());
                boardGameFixedData.setMinimumAge(boardGameFromBGG.minAge().getValue());
                boardGameFixedData.setMinimumPlayers(boardGameFromBGG.players().getMinimum().getValue());
                boardGameFixedData.setMaximumPlayers(boardGameFromBGG.players().getMaximum().getValue());
                boardGameFixedData.setAveragePlaytimeInMinutes(boardGameFromBGG.playtime().getAverage().getValue());
                boardGameFixedData.setMinimumPlaytimeInMinutes(boardGameFromBGG.playtime().getMinimum().getValue());
                boardGameFixedData.setMaximumPlaytimeInMinutes(boardGameFromBGG.playtime().getMaximum().getValue());

                return boardGameFixedDataRepository.save(boardGameFixedData);

            },
            exceptionToError
        );

    }

    public <E> Either<E, ImmutablePair<BoardGame, BoardGameReference>> saveBoardGame(
        Long dataSourceId,
        BoardGameFromBGG boardGameFromBGG,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, Optional<ImmutablePair<BoardGame, BoardGameReference>>> optionalBoardGameWithReferenceContainer =
            wrapWithErrorHandling(
                () -> boardGameRepository.findByDataSourceIdAndExternalId(dataSourceId, boardGameFromBGG.id()),
                exceptionToError
            );

        Either<E, ImmutablePair<BoardGame, BoardGameReference>> boardGameWithReferenceContainer =
            optionalBoardGameWithReferenceContainer.flatMap(
                optionalBoardGameWithReference -> optionalBoardGameWithReference
                    .map(boardGameWithReference -> {

                        BoardGame oldBoardGame = boardGameWithReference.getLeft();
                        BoardGameReference boardGameReference = boardGameWithReference.getRight();

                        return updateBoardGame(oldBoardGame, boardGameFromBGG, exceptionToError)
                        .map(updatedBoardGame -> new ImmutablePair<>(updatedBoardGame, boardGameReference));

                    })
                    .orElseGet(() ->
                        createBoardGame(boardGameFromBGG, exceptionToError)
                        .flatMap(newBoardGame ->
                            createBoardGameReference(
                                newBoardGame.getId(),
                                dataSourceId,
                                boardGameFromBGG,
                                exceptionToError
                            )
                            .map(boardGameReference -> new ImmutablePair<>(newBoardGame, boardGameReference))
                        )
                    )
            );

        return boardGameWithReferenceContainer;

    }

    private <E> Either<E, BoardGameReference> createBoardGameReference(
        Long boardGameId,
        Long dataSourceId,
        BoardGameFromBGG boardGameFromBGG,
        Function<Throwable, E> exceptionToError
    ) {

        BoardGameReference boardGameReference = new BoardGameReference();
        boardGameReference.setBoardGameId(boardGameId);
        boardGameReference.setDataSourceId(dataSourceId);
        boardGameReference.setExternalId(boardGameFromBGG.id());

        return wrapWithErrorHandling(
            () -> boardGameReferenceRepository.save(boardGameReference),
            exceptionToError
        );

    }

    private <E> Either<E, BoardGame> createBoardGame(
        BoardGameFromBGG boardGameFromBGG,
        Function<Throwable, E> exceptionToError
    ) {

        BoardGame boardGame = new BoardGame();
        BoardGameService.combine(boardGame, boardGameFromBGG);

        return wrapWithErrorHandling(
            () -> boardGameRepository.save(boardGame),
            exceptionToError
        );

    }

    private <E> Either<E, BoardGame> updateBoardGame(
        BoardGame boardGame,
        BoardGameFromBGG boardGameFromBGG,
        Function<Throwable, E> exceptionToError
    ) {

        BoardGameService.combine(boardGame, boardGameFromBGG);

        return wrapWithErrorHandling(
            () -> boardGameRepository.save(boardGame),
            exceptionToError
        );

    }

    public <E> Either<E, Collection<BoardGameReference>> saveBoardGameReferences(
        Long dataSourceId,
        Collection<Link> inputs,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, ImmutablePair<BoardGame, BoardGameReference>>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                inputs,
                links -> wrapWithErrorHandling(
                    () -> boardGameRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                pairOfBoardGameAndBoardGameReference -> pairOfBoardGameAndBoardGameReference.getRight().getExternalId()
            );

        Either<E, Map<Boolean, Collection<ImmutablePair<Link, BoardGame>>>> boardGamesByStatusContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        BoardGameService::linkToBoardGame,
                        boardGameRepository::saveAll,
                        exceptionToError
                    ),
                    Either::right,
                    zipper(ImmutablePair::of),
                    orderedZipper(
                        linkToKey,
                        pairOfBoardGameAndBoardGameReference -> pairOfBoardGameAndBoardGameReference.getRight().getExternalId(),
                        Function.identity(),
                        ImmutablePair::getLeft,
                        ImmutablePair::of
                    ),
                    (newItems, existingItems) -> Map.of(true, newItems, false, existingItems)
                )
            );

        Either<E, Collection<BoardGameReference>> boardGameReferencesContainer =
            boardGamesByStatusContainer.flatMap(boardGamesByStatus ->
                processItems(
                    boardGamesByStatus,
                    newInputs -> mapAndProcess(
                        newInputs,
                        linkAndBoardGame -> {

                            BoardGameReference boardGameReference = new BoardGameReference();
                            boardGameReference.setDataSourceId(dataSourceId);
                            boardGameReference.setBoardGameId(linkAndBoardGame.getRight().getId());
                            boardGameReference.setExternalId(linkAndBoardGame.getLeft().id());
                            boardGameReference.setAverageRating(null);

                            return boardGameReference;
                        },
                        boardGameReferenceRepository::saveAll,
                        exceptionToError
                    ),
                    existingInputs -> mapAndProcess(
                        existingInputs,
                        linkAndBoardGame -> linkAndBoardGame.getLeft().id(),
                        externalIds ->
                            boardGameReferenceRepository.findByDataSourceIdAndExternalIdIn(dataSourceId, externalIds)
                                .collect(Collectors.toList()),
                        exceptionToError
                    ),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        return boardGameReferencesContainer;

    }

    private static BoardGame linkToBoardGame(Link link) {

        BoardGame boardGame = new BoardGame();
        boardGame.setName(link.value());

        return boardGame;

    }

    private static void combine(BoardGame boardGame, BoardGameFromBGG boardGameFromBGG) {

        String mainName = boardGameFromBGG.names().stream()
                .filter(name -> NameType.PRIMARY.equals(name.type()))
                .findFirst()
                .map(Name::value)
                .orElse(null);

        boardGame.setName(mainName);
        boardGame.setDescription(boardGameFromBGG.description());

    }






    public <E> Either<E, Collection<BoardGame>> getAllBoardGames(
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            boardGameRepository::findAll,
            exceptionToError
        );

    }

    public <E> Either<E, Collection<BoardGameReference>> getAllBoardGameReferences(
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            boardGameReferenceRepository::findAll,
            exceptionToError
        );

    }

}
