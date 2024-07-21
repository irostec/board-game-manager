package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Artist;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ArtistReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameArtist;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ArtistRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ArtistReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameArtistRepository;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.utility.PartialFunctionDefinition;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.orderedZipper;
import static com.irostec.boardgamemanager.common.utility.Functions.zipper;
import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;
import static com.irostec.boardgamemanager.common.utility.Functions.processItems;
import static com.irostec.boardgamemanager.common.utility.Functions.mapAndProcess;

@Component
@AllArgsConstructor
public class ArtistService {

    private static final Function<Link, Artist> LINK_TO_ARTIST = link -> {

        Artist artist = new Artist();
        artist.setName(link.value());

        return artist;

    };

    private final ArtistRepository artistRepository;
    private final ArtistReferenceRepository artistReferenceRepository;
    private final BoardGameArtistRepository boardGameArtistRepository;

    public <E> Either<E, Collection<BoardGameArtist>> saveBoardGameArtists(
        long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, ImmutablePair<Artist, ArtistReference>>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> artistRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                pairOfBoardGameAndBoardGameReference -> pairOfBoardGameAndBoardGameReference.getRight().getExternalId()
            );

        Either<E, Map<Boolean, Collection<ImmutablePair<Link, Artist>>>> artistsByStatusContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        LINK_TO_ARTIST,
                        artistRepository::saveAll,
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

        Either<E, Collection<ArtistReference>> artistReferencesContainer =
            artistsByStatusContainer.flatMap(artistsByStatus ->
                processItems(
                    artistsByStatus,
                    newInputs -> mapAndProcess(
                        newInputs,
                        linkAndArtist -> {

                            ArtistReference artistReference = new ArtistReference();
                            artistReference.setDataSourceId(dataSourceId);
                            artistReference.setArtistId(linkAndArtist.getRight().getId());
                            artistReference.setExternalId(linkAndArtist.getLeft().id());

                            return artistReference;
                        },
                        artistReferenceRepository::saveAll,
                        exceptionToError
                    ),
                    existingInputs -> mapAndProcess(
                        existingInputs,
                        linkAndArtist -> linkAndArtist.getLeft().id(),
                        externalIds ->
                            artistReferenceRepository.findByDataSourceIdAndExternalIdIn(dataSourceId, externalIds)
                                .collect(Collectors.toList()),
                        exceptionToError
                    ),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGameArtist>> boardGameArtistsContainer =
            artistReferencesContainer.flatMap(artistReferences ->
                mapAndProcess(
                    artistReferences,
                    artistReference -> {

                        BoardGameArtist boardGameArtist = new BoardGameArtist();
                        boardGameArtist.setBoardGameId(boardGameId);
                        boardGameArtist.setArtistId(artistReference.getArtistId());

                        return boardGameArtist;

                    },
                    boardGameArtistRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameArtistsContainer;

    }

}
