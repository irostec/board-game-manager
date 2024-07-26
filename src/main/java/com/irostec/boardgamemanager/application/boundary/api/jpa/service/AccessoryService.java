package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.AccessoryReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.AccessoryRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameAccessoryRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.AccessoryReferenceCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameAccessoryCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
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
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final AccessoryReferenceRepository accessoryReferenceRepository;
    private final BoardGameAccessoryRepository boardGameAccessoryRepository;
    private final AccessoryReferenceCollectionFilter accessoryReferenceCollectionFilter;
    private final BoardGameAccessoryCollectionFilter boardGameAccessoryCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<AccessoryReference> saveAccessoryReferences(
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, AccessoryReference> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links,
                dataSourceId,
                accessoryReferenceCollectionFilter,
                Link::id,
                AccessoryReference::getExternalId
            );

        Collection<Accessory> newAccessories = wrapWithErrorHandling(() ->
            accessoryRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        link -> {

                            Accessory accessory = new Accessory();
                            accessory.setName(link.value());

                            return accessory;

                        }
                    )
                    .toList()
            )
        );

        Collection<ImmutablePair<Link, Accessory>> newLinksWithAccessories =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newAccessories.stream().sorted(Comparator.comparing(Accessory::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<AccessoryReference> newAccessoryReferences = wrapWithErrorHandling(() ->
            accessoryReferenceRepository.saveAll(
                newLinksWithAccessories.stream().map(
                    linkWithAccessory -> {

                        AccessoryReference accessoryReference = new AccessoryReference();
                        accessoryReference.setDataSourceId(dataSourceId);
                        accessoryReference.setAccessoryId(linkWithAccessory.getRight().getId());
                        accessoryReference.setExternalId(linkWithAccessory.getLeft().id());

                        return accessoryReference;
                    }
                )
                .toList()
            )
        );

        Collection<AccessoryReference> existingAccessoryReferences = partialFunctionDefinition.getImage();

        return Stream.concat(newAccessoryReferences.stream(), existingAccessoryReferences.stream()).toList();

    }

    private Collection<BoardGameAccessory> saveBoardGameAccessories(
        long boardGameId,
        Collection<AccessoryReference> accessoryReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<AccessoryReference, BoardGameAccessory> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                accessoryReferences,
                boardGameId,
                boardGameAccessoryCollectionFilter,
                AccessoryReference::getAccessoryId,
                BoardGameAccessory::getAccessoryId
            );

        Collection<BoardGameAccessory> newBoardGameAccessories = wrapWithErrorHandling(() ->
            boardGameAccessoryRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        accessoryReference -> {

                            BoardGameAccessory boardGameAccessory = new BoardGameAccessory();
                            boardGameAccessory.setBoardGameId(boardGameId);
                            boardGameAccessory.setAccessoryId(accessoryReference.getAccessoryId());

                            return boardGameAccessory;
                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameAccessory> existingBoardGameAccessories = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameAccessories.stream(), existingBoardGameAccessories.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameAccessory> saveBoardGameAccessories(
        long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<AccessoryReference> accessoryReferences = saveAccessoryReferences(dataSourceId, links);

        return saveBoardGameAccessories(boardGameId, accessoryReferences);

    }

}
