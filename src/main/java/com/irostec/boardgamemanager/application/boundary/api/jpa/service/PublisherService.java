package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.PublisherRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.PublisherReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGamePublisherRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGamePublisherCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.PublisherReferenceCollectionFilter;
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
public class PublisherService {

    private static final Function<Link, Publisher> LINK_TO_PUBLISHER = link -> {

        Publisher publisher = new Publisher();
        publisher.setName(link.value());

        return publisher;

    };

    private final PublisherRepository publisherRepository;
    private final PublisherReferenceRepository publisherReferenceRepository;
    private final BoardGamePublisherRepository boardGamePublisherRepository;
    private final BoardGamePublisherCollectionFilter boardGamePublisherCollectionFilter;
    private final PublisherReferenceCollectionFilter publisherReferenceCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<PublisherReference> savePublisherReferences(
        DataSource dataSource,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<Link, PublisherReference> filteringResult =
            entityCollectionProcessor.apply(
                links,
                dataSource,
                publisherReferenceCollectionFilter,
                Link::id,
                PublisherReference::getExternalId
            );

        Collection<Publisher> newPublishers = publisherRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(LINK_TO_PUBLISHER)
                .toList()
        );

        filteringResult.getMappings().forEach(
            (existingLink, existingPublisherReference) -> {
                existingPublisherReference.getPublisher().setName(existingLink.value());
            });

        Collection<ImmutablePair<Link, Publisher>> newLinksWithPublishers =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newPublishers.stream().sorted(Comparator.comparing(Publisher::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<PublisherReference> newPublisherReferences = publisherReferenceRepository.saveAll(
            newLinksWithPublishers.stream().map(
                linkWithPublisher -> {

                    PublisherReference publisherReference = new PublisherReference();
                    publisherReference.setDataSource(dataSource);
                    publisherReference.setPublisher(linkWithPublisher.getRight());
                    publisherReference.setExternalId(linkWithPublisher.getLeft().id());

                    return publisherReference;
                }
            )
            .toList()
        );

        Collection<PublisherReference> existingPublisherReferences = filteringResult.getMappings().values();

        return Stream.concat(newPublisherReferences.stream(), existingPublisherReferences.stream()).toList();

    }

    private Collection<BoardGamePublisher> saveBoardGamePublishers(
        BoardGame boardGame,
        Collection<PublisherReference> publisherReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<PublisherReference, BoardGamePublisher> filteringResult =
            entityCollectionProcessor.apply(
                publisherReferences,
                boardGame,
                boardGamePublisherCollectionFilter,
                PublisherReference::getPublisher,
                BoardGamePublisher::getPublisher
            );

        Collection<BoardGamePublisher> newBoardGamePublishers = boardGamePublisherRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    publisherReference -> {

                        BoardGamePublisher boardGamePublisher = new BoardGamePublisher();
                        boardGamePublisher.setBoardGame(boardGame);
                        boardGamePublisher.setPublisher(publisherReference.getPublisher());

                        return boardGamePublisher;

                    }
                )
                .toList()
        );

        Collection<BoardGamePublisher> existingBoardGamePublishers = filteringResult.getMappings().values();

        return Stream.concat(newBoardGamePublishers.stream(), existingBoardGamePublishers.stream()).toList();

    }

    @Transactional
    public Collection<BoardGamePublisher> saveBoardGamePublishers(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<PublisherReference> publisherReferences = savePublisherReferences(dataSource, links);

        return saveBoardGamePublishers(boardGame, publisherReferences);

    }

}
