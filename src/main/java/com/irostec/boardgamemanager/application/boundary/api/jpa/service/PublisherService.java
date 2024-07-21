package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.ImmutableMap;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGamePublisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Publisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.PublisherReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.PublisherRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.PublisherReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGamePublisherRepository;
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
public class PublisherService {

    private static final Function<Link, Publisher> LINK_TO_PUBLISHER = link -> {

        Publisher publisher = new Publisher();
        publisher.setName(link.value());

        return publisher;

    };

    private final PublisherRepository publisherRepository;
    private final PublisherReferenceRepository publisherReferenceRepository;
    private final BoardGamePublisherRepository boardGamePublisherRepository;

    public <E> Either<E, Collection<BoardGamePublisher>> saveBoardGamePublishers(
        long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, ImmutablePair<Publisher, PublisherReference>>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> publisherRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                pairOfBoardGameAndBoardGameReference -> pairOfBoardGameAndBoardGameReference.getRight().getExternalId()
            );

        Either<E, Map<Boolean, Collection<ImmutablePair<Link, Publisher>>>> publishersByStatusContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        LINK_TO_PUBLISHER,
                        publisherRepository::saveAll,
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
                    (newItems, existingItems) -> ImmutableMap.of(true, newItems, false, existingItems)
                )
            );

        Either<E, Collection<PublisherReference>> publisherReferencesContainer =
            publishersByStatusContainer.flatMap(publishersByStatus ->
                processItems(
                    publishersByStatus,
                    newInputs -> mapAndProcess(
                        newInputs,
                        linkAndPublisher -> {

                            PublisherReference publisherReference = new PublisherReference();
                            publisherReference.setDataSourceId(dataSourceId);
                            publisherReference.setPublisherId(linkAndPublisher.getRight().getId());
                            publisherReference.setExternalId(linkAndPublisher.getLeft().id());

                            return publisherReference;
                        },
                        publisherReferenceRepository::saveAll,
                        exceptionToError
                    ),
                    existingInputs -> mapAndProcess(
                        existingInputs,
                        linkAndPublisher -> linkAndPublisher.getLeft().id(),
                        externalIds ->
                            publisherReferenceRepository.findByDataSourceIdAndExternalIdIn(dataSourceId, externalIds)
                                .collect(Collectors.toList()),
                        exceptionToError
                    ),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGamePublisher>> boardGamePublishersContainer =
            publisherReferencesContainer.flatMap(publisherReferences ->
                mapAndProcess(
                    publisherReferences,
                    publisherReference -> {

                        BoardGamePublisher boardGamePublisher = new BoardGamePublisher();
                        boardGamePublisher.setBoardGameId(boardGameId);
                        boardGamePublisher.setPublisherId(publisherReference.getPublisherId());

                        return boardGamePublisher;

                    },
                    boardGamePublisherRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGamePublishersContainer;

    }

}
