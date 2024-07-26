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

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final BoardGameReferenceRepository boardGameReferenceRepository;
    private final BoardGameFixedDataRepository boardGameFixedDataRepository;
    private final BoardGameReferenceCollectionFilter boardGameReferenceCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    @Transactional(rollbackFor = Throwable.class)
    public BoardGameFixedData saveBoardGameFixedData(
        Long boardGameId,
        BoardGameFromBGG boardGameFromBGG
    ) throws BoundaryException {

        BoardGameFixedData boardGameFixedData =
            wrapWithErrorHandling(() -> boardGameFixedDataRepository.findByBoardGameId(boardGameId))
                .orElseGet(BoardGameFixedData::new);

        boardGameFixedData.setBoardGameId(boardGameId);
        boardGameFixedData.setYearOfPublication(boardGameFromBGG.yearPublished());
        boardGameFixedData.setMinimumAge(boardGameFromBGG.minAge().getValue());
        boardGameFixedData.setMinimumPlayers(boardGameFromBGG.players().getMinimum().getValue());
        boardGameFixedData.setMaximumPlayers(boardGameFromBGG.players().getMaximum().getValue());
        boardGameFixedData.setAveragePlaytimeInMinutes(boardGameFromBGG.playtime().getAverage().getValue());
        boardGameFixedData.setMinimumPlaytimeInMinutes(boardGameFromBGG.playtime().getMinimum().getValue());
        boardGameFixedData.setMaximumPlaytimeInMinutes(boardGameFromBGG.playtime().getMaximum().getValue());

        return wrapWithErrorHandling(() -> boardGameFixedDataRepository.saveAndFlush(boardGameFixedData));

    }

    @Transactional(rollbackFor = Throwable.class)
    public ImmutablePair<BoardGame, BoardGameReference> saveBoardGame(
        Long dataSourceId,
        BoardGameFromBGG boardGameFromBGG
    ) throws BoundaryException {

        ImmutablePair<BoardGame, BoardGameReference> boardGameWithReference =
        wrapWithErrorHandling(() -> boardGameRepository.findByDataSourceIdAndExternalId(dataSourceId, boardGameFromBGG.id()))
                .orElseGet(() -> new ImmutablePair<>(new BoardGame(), new BoardGameReference()));

        BoardGame previousBoardGame = boardGameWithReference.getLeft();
        BoardGameService.combine(previousBoardGame, boardGameFromBGG);
        BoardGame updatedBoardGame =
            wrapWithErrorHandling(() -> boardGameRepository.saveAndFlush(previousBoardGame));

        BoardGameReference previousBoardGameReference = boardGameWithReference.getRight();
        BoardGameService.combine(previousBoardGameReference, updatedBoardGame.getId(), dataSourceId, boardGameFromBGG);
        BoardGameReference updateBoardGameReference =
            wrapWithErrorHandling(() -> boardGameReferenceRepository.saveAndFlush(previousBoardGameReference));

        return new ImmutablePair<>(updatedBoardGame, updateBoardGameReference);

    }

    public Collection<BoardGameReference> saveBoardGameReferences(
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, BoardGameReference> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links,
                dataSourceId,
                boardGameReferenceCollectionFilter,
                Link::id,
                BoardGameReference::getExternalId
            );

        Collection<BoardGame> newBoardGames = wrapWithErrorHandling(() ->
            boardGameRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        link -> {

                            BoardGame boardGame = new BoardGame();
                            boardGame.setName(link.value());

                            return boardGame;

                        }
                    )
                    .toList()
            )
        );

        Collection<ImmutablePair<Link, BoardGame>> newLinksWithBoardGames =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newBoardGames.stream().sorted(Comparator.comparing(BoardGame::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<BoardGameReference> newBoardGameReferences = wrapWithErrorHandling(() ->
            boardGameReferenceRepository.saveAll(
                newLinksWithBoardGames.stream().map(
                    linkWithBoardGame -> {

                        BoardGameReference boardGameReference = new BoardGameReference();
                        boardGameReference.setDataSourceId(dataSourceId);
                        boardGameReference.setBoardGameId(linkWithBoardGame.getRight().getId());
                        boardGameReference.setExternalId(linkWithBoardGame.getLeft().id());
                        boardGameReference.setAverageRating(null);

                        return boardGameReference;
                    }
                )
                .toList()
            )
        );

        Collection<BoardGameReference> existingBoardGameReferences = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameReferences.stream(), existingBoardGameReferences.stream()).toList();

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

    private static void combine(
        BoardGameReference boardGameReference,
        long boardGameId,
        long dataSourceId,
        BoardGameFromBGG boardGameFromBGG
    ) {

        boardGameReference.setBoardGameId(boardGameId);
        boardGameReference.setDataSourceId(dataSourceId);
        boardGameReference.setExternalId(boardGameFromBGG.id());

    }

}
