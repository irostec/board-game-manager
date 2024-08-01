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
        DataSource dataSource,
        Collection<Link> links
    ) {

        EntityCollectionProcessor.Result<Link, DesignerReference> filteringResult =
            entityCollectionProcessor.apply(
                links,
                dataSource,
                designerReferenceCollectionFilter,
                Link::id,
                DesignerReference::getExternalId
            );

        Collection<Designer> newDesigners = designerRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(LINK_TO_DESIGNER)
                .toList()
        );

        filteringResult.getMappings().forEach(
            (existingLink, existingDesignerReference) -> {
                existingDesignerReference.getDesigner().setName(existingLink.value());
            });

        Collection<ImmutablePair<Link, Designer>> newLinksWithDesigners =
            Streams.zip(
                links.stream().sorted(Comparator.comparing(Link::value)),
                newDesigners.stream().sorted(Comparator.comparing(Designer::getName)),
                ImmutablePair::of
            )
            .toList();

        Collection<DesignerReference> newDesignerReferences =
            designerReferenceRepository.saveAll(
                newLinksWithDesigners.stream().map(
                    linkWithDesigner -> {

                        DesignerReference designerReference = new DesignerReference();
                        designerReference.setDataSource(dataSource);
                        designerReference.setDesigner(linkWithDesigner.getRight());
                        designerReference.setExternalId(linkWithDesigner.getLeft().id());

                        return designerReference;
                    }
                )
                .toList()
            );

        Collection<DesignerReference> existingDesignerReferences = filteringResult.getMappings().values();

        return Stream.concat(newDesignerReferences.stream(), existingDesignerReferences.stream()).toList();

    }

    private Collection<BoardGameDesigner> saveBoardGameDesigners(
        BoardGame boardGame,
        Collection<DesignerReference> designerReferences
    ) throws BoundaryException {

        EntityCollectionProcessor.Result<DesignerReference, BoardGameDesigner> filteringResult =
            entityCollectionProcessor.apply(
                designerReferences,
                boardGame,
                boardGameDesignerCollectionFilter,
                DesignerReference::getDesigner,
                BoardGameDesigner::getDesigner
            );

        Collection<BoardGameDesigner> newBoardGameDesigners = boardGameDesignerRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    designerReference -> {

                        BoardGameDesigner boardGameDesigner = new BoardGameDesigner();
                        boardGameDesigner.setBoardGame(boardGame);
                        boardGameDesigner.setDesigner(designerReference.getDesigner());

                        return boardGameDesigner;

                    }
                )
                .toList()
        );

        Collection<BoardGameDesigner> existingBoardGameDesigners = filteringResult.getMappings().values();

        return Stream.concat(newBoardGameDesigners.stream(), existingBoardGameDesigners.stream()).toList();

    }

    @Transactional
    public Collection<BoardGameDesigner> saveBoardGameDesigners(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<DesignerReference> designerReferences = saveDesignerReferences(dataSource, links);

        return saveBoardGameDesigners(boardGame, designerReferences);

    }

}
