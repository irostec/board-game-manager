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

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

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
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, PublisherReference> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links,
                dataSourceId,
                publisherReferenceCollectionFilter,
                Link::id,
                PublisherReference::getExternalId
            );

        Collection<Publisher> newPublishers = wrapWithErrorHandling(() ->
            publisherRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(LINK_TO_PUBLISHER)
                    .toList()
            )
        );

        Collection<ImmutablePair<Link, Publisher>> newLinksWithPublishers =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newPublishers.stream().sorted(Comparator.comparing(Publisher::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<PublisherReference> newPublisherReferences = wrapWithErrorHandling(() ->
            publisherReferenceRepository.saveAll(
                newLinksWithPublishers.stream().map(
                    linkWithPublisher -> {

                        PublisherReference publisherReference = new PublisherReference();
                        publisherReference.setDataSourceId(dataSourceId);
                        publisherReference.setPublisherId(linkWithPublisher.getRight().getId());
                        publisherReference.setExternalId(linkWithPublisher.getLeft().id());

                        return publisherReference;
                    }
                )
                .toList()
            )
        );

        Collection<PublisherReference> existingPublisherReferences = partialFunctionDefinition.getImage();

        return Stream.concat(newPublisherReferences.stream(), existingPublisherReferences.stream()).toList();

    }

    private Collection<BoardGamePublisher> saveBoardGamePublishers(
        long boardGameId,
        Collection<PublisherReference> publisherReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<PublisherReference, BoardGamePublisher> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                publisherReferences, boardGameId, boardGamePublisherCollectionFilter, PublisherReference::getPublisherId, BoardGamePublisher::getPublisherId
            );

        Collection<BoardGamePublisher> newBoardGamePublishers = wrapWithErrorHandling(() ->
            boardGamePublisherRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        publisherReference -> {

                            BoardGamePublisher boardGamePublisher = new BoardGamePublisher();
                            boardGamePublisher.setBoardGameId(boardGameId);
                            boardGamePublisher.setPublisherId(publisherReference.getPublisherId());

                            return boardGamePublisher;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGamePublisher> existingBoardGamePublishers = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGamePublishers.stream(), existingBoardGamePublishers.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGamePublisher> saveBoardGamePublishers(
        long boardGameId,
        long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<PublisherReference> publisherReferences = savePublisherReferences(dataSourceId, links);

        return saveBoardGamePublishers(boardGameId, publisherReferences);

    }

}
