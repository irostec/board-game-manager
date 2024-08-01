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
        DataSource dataSource,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<Link, ArtistReference> filteringResult =
            entityCollectionProcessor.apply(
                links,
                dataSource,
                artistReferenceCollectionFilter,
                Link::id,
                ArtistReference::getExternalId
            );

        Collection<Artist> newArtists = artistRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(LINK_TO_ARTIST)
                .toList()
        );

        filteringResult.getMappings().forEach(
            (existingLink, existingArtistReference) -> {
                existingArtistReference.getArtist().setName(existingLink.value());
            });

        Collection<ImmutablePair<Link, Artist>> newLinksWithArtists =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newArtists.stream().sorted(Comparator.comparing(Artist::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<ArtistReference> newArtistReferences = artistReferenceRepository.saveAll(
            newLinksWithArtists.stream().map(
                linkWithArtist -> {

                    ArtistReference artistReference = new ArtistReference();
                    artistReference.setDataSource(dataSource);
                    artistReference.setArtist(linkWithArtist.getRight());
                    artistReference.setExternalId(linkWithArtist.getLeft().id());

                    return artistReference;
                }
            )
            .toList()
        );

        Collection<ArtistReference> existingArtistReferences = filteringResult.getMappings().values();

        return Stream.concat(newArtistReferences.stream(), existingArtistReferences.stream()).toList();

    }

    private Collection<BoardGameArtist> saveBoardGameArtists(
        BoardGame boardGame,
        Collection<ArtistReference> artistReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<ArtistReference, BoardGameArtist> filteringResult =
            entityCollectionProcessor.apply(
                artistReferences,
                boardGame,
                boardGameArtistCollectionFilter,
                ArtistReference::getArtist,
                BoardGameArtist::getArtist
            );

        Collection<BoardGameArtist> newBoardGameArtists = boardGameArtistRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    artistReference -> {

                        BoardGameArtist boardGameArtist = new BoardGameArtist();
                        boardGameArtist.setBoardGame(boardGame);
                        boardGameArtist.setArtist(artistReference.getArtist());

                        return boardGameArtist;

                    }
                )
                .toList()
        );

        Collection<BoardGameArtist> existingBoardGameArtists = filteringResult.getMappings().values();

        return Stream.concat(newBoardGameArtists.stream(), existingBoardGameArtists.stream()).toList();

    }

    @Transactional
    public Collection<BoardGameArtist> saveBoardGameArtists(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<ArtistReference> artistReferences = saveArtistReferences(dataSource, links);

        return saveBoardGameArtists(boardGame, artistReferences);

    }

}
