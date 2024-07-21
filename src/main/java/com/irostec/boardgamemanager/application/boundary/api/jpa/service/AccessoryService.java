package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Accessory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.AccessoryReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameAccessory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.AccessoryReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.AccessoryRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameAccessoryRepository;
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
public class AccessoryService {

    private static final Function<Link, Accessory> LINK_TO_ACCESSORY = link -> {

        Accessory accessory = new Accessory();
        accessory.setName(link.value());

        return accessory;

    };

    private final AccessoryRepository accessoryRepository;
    private final AccessoryReferenceRepository accessoryReferenceRepository;
    private final BoardGameAccessoryRepository boardGameAccessoryRepository;

    public <E> Either<E, Collection<BoardGameAccessory>> saveBoardGameAccessories(
        long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, ImmutablePair<Accessory, AccessoryReference>>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> accessoryRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                pairOfAccessoryAndAccessoryReference -> pairOfAccessoryAndAccessoryReference.getRight().getExternalId()
            );

        Either<E, Map<Boolean, Collection<ImmutablePair<Link, Accessory>>>> accessoriesByStatusContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        LINK_TO_ACCESSORY,
                        accessoryRepository::saveAll,
                        exceptionToError
                    ),
                    Either::right,
                    zipper(ImmutablePair::of),
                    orderedZipper(
                        linkToKey,
                        pairOfAccessoryAndAccessoryReference -> pairOfAccessoryAndAccessoryReference.getRight().getExternalId(),
                        Function.identity(),
                        ImmutablePair::getLeft,
                        ImmutablePair::of
                    ),
                    (newItems, existingItems) -> Map.of(true, newItems, false, existingItems)
                )
            );

        Either<E, Collection<AccessoryReference>> accessoryReferencesContainer =
            accessoriesByStatusContainer.flatMap(accessoriesByStatus ->
                processItems(
                    accessoriesByStatus,
                    newInputs -> mapAndProcess(
                        newInputs,
                        linkAndAccessory -> {

                            AccessoryReference accessoryReference = new AccessoryReference();
                            accessoryReference.setDataSourceId(dataSourceId);
                            accessoryReference.setAccessoryId(linkAndAccessory.getRight().getId());
                            accessoryReference.setExternalId(linkAndAccessory.getLeft().id());

                            return accessoryReference;
                        },
                        accessoryReferenceRepository::saveAll,
                        exceptionToError
                    ),
                    existingInputs -> mapAndProcess(
                        existingInputs,
                        linkAndAccessory -> linkAndAccessory.getLeft().id(),
                        externalIds ->
                            accessoryReferenceRepository.findByDataSourceIdAndExternalIdIn(dataSourceId, externalIds)
                                .collect(Collectors.toList()),
                        exceptionToError
                    ),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGameAccessory>> boardGameAccessoriesContainer =
            accessoryReferencesContainer.flatMap(accessoryReferences ->
                mapAndProcess(
                    accessoryReferences,
                    accessoryReference -> {

                        BoardGameAccessory boardGameAccessory = new BoardGameAccessory();
                        boardGameAccessory.setBoardGameId(boardGameId);
                        boardGameAccessory.setAccessoryId(accessoryReference.getAccessoryId());

                        return boardGameAccessory;
                    },
                    boardGameAccessoryRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameAccessoriesContainer;

    }

}
