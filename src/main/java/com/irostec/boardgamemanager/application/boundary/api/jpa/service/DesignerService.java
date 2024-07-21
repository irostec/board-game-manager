package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.google.common.collect.ImmutableMap;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameDesigner;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Designer;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DesignerReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameDesignerRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DesignerReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DesignerRepository;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.utility.PartialFunctionDefinition;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;

import static com.irostec.boardgamemanager.common.utility.Functions.orderedZipper;
import static com.irostec.boardgamemanager.common.utility.Functions.zipper;
import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;
import static com.irostec.boardgamemanager.common.utility.Functions.processItems;
import static com.irostec.boardgamemanager.common.utility.Functions.mapAndProcess;

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

    public <E> Either<E, Collection<BoardGameDesigner>> saveBoardGameDesigners(
        long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, ImmutablePair<Designer, DesignerReference>>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> designerRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                pairOfBoardGameAndBoardGameReference -> pairOfBoardGameAndBoardGameReference.getRight().getExternalId()
            );

        Either<E, Map<Boolean, Collection<ImmutablePair<Link, Designer>>>> designersByStatusContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        LINK_TO_DESIGNER,
                        designerRepository::saveAll,
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

        Either<E, Collection<DesignerReference>> designerReferencesContainer =
            designersByStatusContainer.flatMap(designersByStatus ->
                processItems(
                    designersByStatus,
                    newInputs -> mapAndProcess(
                        newInputs,
                        linkAndDesigner -> {

                            DesignerReference designerReference = new DesignerReference();
                            designerReference.setDataSourceId(dataSourceId);
                            designerReference.setDesignerId(linkAndDesigner.getRight().getId());
                            designerReference.setExternalId(linkAndDesigner.getLeft().id());

                            return designerReference;
                        },
                        designerReferenceRepository::saveAll,
                        exceptionToError
                    ),
                    existingInputs -> mapAndProcess(
                        existingInputs,
                        linkAndDesigner -> linkAndDesigner.getLeft().id(),
                        externalIds ->
                            designerReferenceRepository.findByDataSourceIdAndExternalIdIn(dataSourceId, externalIds)
                                .collect(Collectors.toList()),
                        exceptionToError
                    ),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGameDesigner>> boardGameDesignersContainer =
            designerReferencesContainer.flatMap(designerReferences ->
                mapAndProcess(
                    designerReferences,
                    designerReference -> {

                        BoardGameDesigner boardGameDesigner = new BoardGameDesigner();
                        boardGameDesigner.setBoardGameId(boardGameId);
                        boardGameDesigner.setDesignerId(designerReference.getDesignerId());

                        return boardGameDesigner;

                    },
                    boardGameDesignerRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameDesignersContainer;

    }

}
