package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameDesignerRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DesignerReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DesignerRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameDesignerCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.DesignerReferenceCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.error.BoundaryException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.transaction.annotation.Transactional;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class DesignerService {

    private static final Function<Link, Designer> LINK_TO_DESIGNER = link -> {

        Designer designer = new Designer();
        designer.setName(link.value());

        return designer;

    };

    private final DesignerRepository designerRepository;
    private final DesignerReferenceRepository designerReferenceRepository;
    private final BoardGameDesignerRepository boardGameDesignerRepository;
    private final BoardGameDesignerCollectionFilter boardGameDesignerCollectionFilter;
    private final DesignerReferenceCollectionFilter designerReferenceCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<DesignerReference> saveDesignerReferences(
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, DesignerReference> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links,
                dataSourceId,
                designerReferenceCollectionFilter,
                Link::id,
                DesignerReference::getExternalId
            );

        Collection<Designer> newDesigners = wrapWithErrorHandling(() ->
            designerRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(LINK_TO_DESIGNER)
                    .toList()
            )
        );

        Collection<ImmutablePair<Link, Designer>> newLinksWithDesigners =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newDesigners.stream().sorted(Comparator.comparing(Designer::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<DesignerReference> newDesignerReferences = wrapWithErrorHandling(() ->
            designerReferenceRepository.saveAll(
                newLinksWithDesigners.stream().map(
                    linkWithDesigner -> {

                        DesignerReference designerReference = new DesignerReference();
                        designerReference.setDataSourceId(dataSourceId);
                        designerReference.setDesignerId(linkWithDesigner.getRight().getId());
                        designerReference.setExternalId(linkWithDesigner.getLeft().id());

                        return designerReference;
                    }
                )
                .toList()
            )
        );

        Collection<DesignerReference> existingDesignerReferences = partialFunctionDefinition.getImage();

        return Stream.concat(newDesignerReferences.stream(), existingDesignerReferences.stream()).toList();

    }

    private Collection<BoardGameDesigner> saveBoardGameDesigners(
        long boardGameId,
        Collection<DesignerReference> designerReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<DesignerReference, BoardGameDesigner> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                designerReferences, boardGameId, boardGameDesignerCollectionFilter, DesignerReference::getDesignerId, BoardGameDesigner::getDesignerId
            );

        Collection<BoardGameDesigner> newBoardGameDesigners = wrapWithErrorHandling(() ->
            boardGameDesignerRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        designerReference -> {

                            BoardGameDesigner boardGameDesigner = new BoardGameDesigner();
                            boardGameDesigner.setBoardGameId(boardGameId);
                            boardGameDesigner.setDesignerId(designerReference.getDesignerId());

                            return boardGameDesigner;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameDesigner> existingBoardGameDesigners = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameDesigners.stream(), existingBoardGameDesigners.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameDesigner> saveBoardGameDesigners(
        long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<DesignerReference> designerReferences = saveDesignerReferences(dataSourceId, links);

        return saveBoardGameDesigners(boardGameId, designerReferences);

    }

}
