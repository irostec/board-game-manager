package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameFamilyRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.FamilyRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameFamilyCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.FamilyCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.error.BoundaryException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final BoardGameFamilyRepository boardGameFamilyRepository;
    private final FamilyCollectionFilter familyCollectionFilter;
    private final BoardGameFamilyCollectionFilter boardGameFamilyCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<Family> saveFamilies(DataSource dataSource, Collection<Link> links) {

        EntityCollectionProcessor.Result<Link, Family> filteringResult =
            entityCollectionProcessor.apply(
                links, dataSource, familyCollectionFilter, Link::id, Family::getExternalId
            );

        BiConsumer<Link, Family> familyConsumer = (link, family) -> {

            family.setDataSource(dataSource);
            family.setExternalId(link.id());
            family.setName(link.value());

        };

        Collection<Family> newFamilies = familyRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    link -> {

                        Family newFamily = new Family();
                        familyConsumer.accept(link, newFamily);

                        return newFamily;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(familyConsumer);

        return Stream.concat(newFamilies.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    private Collection<BoardGameFamily> saveBoardGameFamilies(BoardGame boardGame, Collection<Family> families) {

        EntityCollectionProcessor.Result<Family, BoardGameFamily> filteringResult =
            entityCollectionProcessor.apply(
                families,
                boardGame,
                boardGameFamilyCollectionFilter,
                Function.identity(),
                BoardGameFamily::getFamily
            );

        BiConsumer<Family, BoardGameFamily> familyConsumer =
            (family, boardGameFamily) -> {

                boardGameFamily.setBoardGame(boardGame);
                boardGameFamily.setFamily(family);

            };

        Collection<BoardGameFamily> newBoardGameFamilies = boardGameFamilyRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    family -> {

                        BoardGameFamily newBoardGameFamily = new BoardGameFamily();
                        familyConsumer.accept(family, newBoardGameFamily);

                        return newBoardGameFamily;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(familyConsumer);

        return Stream.concat(newBoardGameFamilies.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    @Transactional
    public Collection<BoardGameFamily> saveBoardGameFamilies(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<Family> families = this.saveFamilies(dataSource, links);

        return saveBoardGameFamilies(boardGame, families);

    }

}
