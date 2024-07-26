package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFamily;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
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
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final BoardGameFamilyRepository boardGameFamilyRepository;
    private final FamilyCollectionFilter familyCollectionFilter;
    private final BoardGameFamilyCollectionFilter boardGameFamilyCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<Family> saveFamilies(Long dataSourceId, Collection<Link> links)
    throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, Family> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links, dataSourceId, familyCollectionFilter, Link::id, Family::getExternalId
            );

        Collection<Family> newFamilies = wrapWithErrorHandling(() ->
            familyRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        link -> {

                            Family family = new Family();
                            family.setDataSourceId(dataSourceId);
                            family.setExternalId(link.id());
                            family.setName(link.value());

                            return family;

                        }
                    )
                    .toList()
            )
        );

        Collection<Family> existingFamilies = partialFunctionDefinition.getImage();

        return Stream.concat(newFamilies.stream(), existingFamilies.stream()).toList();

    }

    private Collection<BoardGameFamily> saveBoardGameFamilies(Long boardGameId, Collection<Family> families)
    throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Family, BoardGameFamily> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                families, boardGameId, boardGameFamilyCollectionFilter, Family::getId, BoardGameFamily::getFamilyId
            );

        Collection<BoardGameFamily> newBoardGameFamilies = wrapWithErrorHandling(() ->
            boardGameFamilyRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        family -> {

                            BoardGameFamily boardGameFamily = new BoardGameFamily();
                            boardGameFamily.setBoardGameId(boardGameId);
                            boardGameFamily.setFamilyId(family.getId());

                            return boardGameFamily;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameFamily> existingBoardGameFamilies = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameFamilies.stream(), existingBoardGameFamilies.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameFamily> saveBoardGameFamilies(
        Long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<Family> families = this.saveFamilies(dataSourceId, links);

        return saveBoardGameFamilies(boardGameId, families);

    }

}
