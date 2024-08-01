package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameFixedDataRepository;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameReferenceCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Name;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.NameType;
import com.irostec.boardgamemanager.common.error.BoundaryException;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final BoardGameReferenceRepository boardGameReferenceRepository;
    private final EntityManagerFactory emf;
    private final BoardGameFixedDataRepository boardGameFixedDataRepository;
    private final BoardGameReferenceCollectionFilter boardGameReferenceCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Optional<ImmutablePair<BoardGame, BoardGameReference>> getBoardGameAndReference(DataSource dataSource, String externalId)
        throws BoundaryException
    {

        TypedQuery<BoardGame> typedQuery = emf.createEntityManager().
            createQuery(
                """
                    SELECT boardGame FROM BoardGame boardGame
                    JOIN FETCH boardGame.references boardGameReference
                    WHERE boardGameReference.dataSource = :dataSource AND boardGameReference.externalId = :externalId
                """,
                BoardGame.class
            );

        typedQuery.setParameter("dataSource", dataSource);
        typedQuery.setParameter("externalId", externalId);

        return typedQuery.getResultStream()
            .findFirst()
            .flatMap(
                boardGame ->
                    boardGame.getReferences().stream()
                        .findFirst()
                        .map(boardGameReference -> new ImmutablePair<>(boardGame, boardGameReference))
                );

    }



    @Transactional
    public ImmutablePair<BoardGame, BoardGameReference> saveBoardGame(
        DataSource dataSource,
        BoardGameFromBGG boardGameFromBGG
    ) {

        Optional<ImmutablePair<BoardGame, BoardGameReference>> optionalBoardGameAndReference =
            this.getBoardGameAndReference(dataSource, boardGameFromBGG.id());

        final boolean newRecord = optionalBoardGameAndReference.isEmpty();

        ImmutablePair<BoardGame, BoardGameReference> boardGameAndReference =
            optionalBoardGameAndReference
                .orElseGet(() -> new ImmutablePair<>(new BoardGame(), new BoardGameReference()));

        BoardGame previousBoardGame = boardGameAndReference.getLeft();

        BoardGameFixedData previousFixedData = Optional.ofNullable(previousBoardGame.getFixedData())
            .orElseGet(BoardGameFixedData::new);
        BoardGameService.combine(previousFixedData, boardGameFromBGG);
        BoardGameFixedData updatedFixedData =
                newRecord ? boardGameFixedDataRepository.save(previousFixedData) : previousFixedData;

        BoardGameService.combine(previousBoardGame, updatedFixedData, boardGameFromBGG);
        BoardGame updatedBoardGame =
            newRecord ? boardGameRepository.save(previousBoardGame) : previousBoardGame;

        BoardGameReference previousBoardGameReference = boardGameAndReference.getRight();
        BoardGameService.combine(previousBoardGameReference, updatedBoardGame, dataSource, boardGameFromBGG);
        BoardGameReference updateBoardGameReference =
            newRecord ? boardGameReferenceRepository.save(previousBoardGameReference) : previousBoardGameReference;

        return new ImmutablePair<>(updatedBoardGame, updateBoardGameReference);

    }

    @Transactional
    public Collection<BoardGameReference> saveBoardGameReferences(
        DataSource dataSource,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<Link, BoardGameReference> filteringResult =
            entityCollectionProcessor.apply(
                links, dataSource, boardGameReferenceCollectionFilter, Link::id, BoardGameReference::getExternalId
            );

        BiConsumer<Link, BoardGame> boardGameConsumer = (link, boardGame) -> {
            boardGame.setName(link.value());
        };

        Collection<BoardGame> newBoardGames = boardGameRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    link -> {

                        BoardGame newBoardGame = new BoardGame();
                        boardGameConsumer.accept(link, newBoardGame);

                        return newBoardGame;

                    }
                )
                .toList()
        );

        Collection<ImmutablePair<Link, BoardGame>> newLinksWithBoardGames =
            Streams.zip(
                filteringResult.getUnmapped().stream().sorted(Comparator.comparing(Link::value)),
                newBoardGames.stream().sorted(Comparator.comparing(BoardGame::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<BoardGameReference> newBoardGameReferences = boardGameReferenceRepository.saveAll(
            newLinksWithBoardGames.stream().map(
                linkWithBoardGame -> {

                    BoardGameReference boardGameReference = new BoardGameReference();
                    boardGameReference.setDataSource(dataSource);
                    boardGameReference.setBoardGame(linkWithBoardGame.getRight());
                    boardGameReference.setExternalId(linkWithBoardGame.getLeft().id());
                    boardGameReference.setAverageRating(null);

                    return boardGameReference;
                }
            )
            .toList()
        );

        Collection<BoardGameReference> existingBoardGameReferences = filteringResult.getMappings().values();

        return Stream.concat(newBoardGameReferences.stream(), existingBoardGameReferences.stream()).toList();

    }

    private static void combine(
        BoardGame boardGame,
        BoardGameFixedData fixedData,
        BoardGameFromBGG boardGameFromBGG
    ) {

        String mainName = boardGameFromBGG.names().stream()
                .filter(name -> NameType.PRIMARY.equals(name.type()))
                .findFirst()
                .map(Name::value)
                .orElse(null);

        boardGame.setName(mainName);
        boardGame.setFixedData(fixedData);
        boardGame.setDescription(boardGameFromBGG.description());

    }

    private static void combine(
        BoardGameReference boardGameReference,
        BoardGame boardGame,
        DataSource dataSource,
        BoardGameFromBGG boardGameFromBGG
    ) {

        boardGameReference.setBoardGame(boardGame);
        boardGameReference.setDataSource(dataSource);
        boardGameReference.setExternalId(boardGameFromBGG.id());

    }

    private static void combine(
        BoardGameFixedData boardGameFixedData,
        BoardGameFromBGG boardGameFromBGG
    ) {

        boardGameFixedData.setYearOfPublication(boardGameFromBGG.yearPublished());
        boardGameFixedData.setMinimumPlayers(boardGameFromBGG.players().getMinimum().getValue());
        boardGameFixedData.setMaximumPlayers(boardGameFromBGG.players().getMaximum().getValue());
        boardGameFixedData.setAveragePlaytimeInMinutes(boardGameFromBGG.playtime().getAverage().getValue());
        boardGameFixedData.setMinimumPlaytimeInMinutes(boardGameFromBGG.playtime().getMinimum().getValue());
        boardGameFixedData.setMaximumPlaytimeInMinutes(boardGameFromBGG.playtime().getMaximum().getValue());
        boardGameFixedData.setMinimumAge(boardGameFromBGG.minAge().getValue());

    }

}
