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
import java.util.function.BiConsumer;
import java.util.stream.Stream;

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
        DataSource dataSource,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<Link, AccessoryReference> filteringResult =
            entityCollectionProcessor.apply(
                links,
                dataSource,
                accessoryReferenceCollectionFilter,
                Link::id,
                AccessoryReference::getExternalId
            );

        BiConsumer<Link, Accessory> accessoryConsumer =
            (link, accessory) -> {
                accessory.setName(link.value());
            };

        Collection<Accessory> newAccessories = accessoryRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    link -> {

                        Accessory newAccessory = new Accessory();
                        accessoryConsumer.accept(link, newAccessory);

                        return newAccessory;

                    }
                )
                .toList()
        );

        Collection<ImmutablePair<Link, Accessory>> newLinksWithAccessories =
            Streams.zip(
                filteringResult.getUnmapped().stream().sorted(Comparator.comparing(Link::value)),
                newAccessories.stream().sorted(Comparator.comparing(Accessory::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<AccessoryReference> newAccessoryReferences = accessoryReferenceRepository.saveAll(
            newLinksWithAccessories.stream().map(
                linkWithAccessory -> {

                    AccessoryReference accessoryReference = new AccessoryReference();
                    accessoryReference.setDataSource(dataSource);
                    accessoryReference.setAccessory(linkWithAccessory.getRight());
                    accessoryReference.setExternalId(linkWithAccessory.getLeft().id());

                    return accessoryReference;
                }
            )
            .toList()
        );

        Collection<AccessoryReference> existingAccessoryReferences = filteringResult.getMappings().values();

        return Stream.concat(newAccessoryReferences.stream(), existingAccessoryReferences.stream()).toList();

    }

    private Collection<BoardGameAccessory> saveBoardGameAccessories(
        BoardGame boardGame,
        Collection<AccessoryReference> accessoryReferences
    ) {

        EntityCollectionProcessor.Result<AccessoryReference, BoardGameAccessory> filteringResult =
            entityCollectionProcessor.apply(
                accessoryReferences,
                boardGame,
                boardGameAccessoryCollectionFilter,
                AccessoryReference::getAccessory,
                BoardGameAccessory::getAccessory
            );

        Collection<BoardGameAccessory> newBoardGameAccessories = boardGameAccessoryRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    accessoryReference -> {

                        BoardGameAccessory boardGameAccessory = new BoardGameAccessory();
                        boardGameAccessory.setBoardGame(boardGame);
                        boardGameAccessory.setAccessory(accessoryReference.getAccessory());

                        return boardGameAccessory;
                    }
                )
                .toList()
        );

        return Stream.concat(newBoardGameAccessories.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    @Transactional
    public Collection<BoardGameAccessory> saveBoardGameAccessories(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<AccessoryReference> accessoryReferences = saveAccessoryReferences(dataSource, links);

        return saveBoardGameAccessories(boardGame, accessoryReferences);

    }

}
