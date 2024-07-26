package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ArtistRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ArtistReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameArtistRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.ArtistReferenceCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameArtistCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.error.BoundaryException;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

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
    private final BoardGameArtistCollectionFilter boardGameArtistCollectionFilter;
    private final ArtistReferenceCollectionFilter artistReferenceCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<ArtistReference> saveArtistReferences(
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, ArtistReference> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links,
                dataSourceId,
                artistReferenceCollectionFilter,
                Link::id,
                ArtistReference::getExternalId
            );

        Collection<Artist> newDesigners = wrapWithErrorHandling(() ->
            artistRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(LINK_TO_ARTIST)
                    .toList()
            )
        );

        Collection<ImmutablePair<Link, Artist>> newLinksWithArtists =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newDesigners.stream().sorted(Comparator.comparing(Artist::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<ArtistReference> newArtistReferences = wrapWithErrorHandling(() ->
            artistReferenceRepository.saveAll(
                newLinksWithArtists.stream().map(
                    linkWithArtist -> {

                        ArtistReference artistReference = new ArtistReference();
                        artistReference.setDataSourceId(dataSourceId);
                        artistReference.setArtistId(linkWithArtist.getRight().getId());
                        artistReference.setExternalId(linkWithArtist.getLeft().id());

                        return artistReference;
                    }
                )
                .toList()
            )
        );

        Collection<ArtistReference> existingArtistReferences = partialFunctionDefinition.getImage();

        return Stream.concat(newArtistReferences.stream(), existingArtistReferences.stream()).toList();

    }

    private Collection<BoardGameArtist> saveBoardGameArtists(
        long boardGameId,
        Collection<ArtistReference> artistReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<ArtistReference, BoardGameArtist> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                artistReferences, boardGameId, boardGameArtistCollectionFilter, ArtistReference::getArtistId, BoardGameArtist::getArtistId
            );

        Collection<BoardGameArtist> newBoardGameArtists = wrapWithErrorHandling(() ->
            boardGameArtistRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        artistReference -> {

                            BoardGameArtist boardGameArtist = new BoardGameArtist();
                            boardGameArtist.setBoardGameId(boardGameId);
                            boardGameArtist.setArtistId(artistReference.getArtistId());

                            return boardGameArtist;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameArtist> existingBoardGameArtists = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameArtists.stream(), existingBoardGameArtists.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameArtist> saveBoardGameArtists(
        long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<ArtistReference> artistReferences = saveArtistReferences(dataSourceId, links);

        return saveBoardGameArtists(boardGameId, artistReferences);

    }

}
