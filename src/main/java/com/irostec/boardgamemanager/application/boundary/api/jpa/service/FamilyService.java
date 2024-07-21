package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameFamily;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Family;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameFamilyRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.FamilyRepository;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.utility.PartialFunctionDefinition;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.zipper;
import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;
import static com.irostec.boardgamemanager.common.utility.Functions.processItems;
import static com.irostec.boardgamemanager.common.utility.Functions.mapAndProcess;

@Component
@AllArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final BoardGameFamilyRepository boardGameFamilyRepository;

    public <E> Either<E, Collection<BoardGameFamily>> saveFamilies(
        Long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, Family>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> familyRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                Family::getExternalId
            );

        Either<E, Collection<Family>> familiesContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        link -> {

                            Family family = new Family();
                            family.setDataSourceId(dataSourceId);
                            family.setExternalId(link.id());
                            family.setName(link.value());

                            return family;

                        },
                        familyRepository::saveAll,
                        exceptionToError
                    ),
                    Either::right,
                    zipper((newInputs, newItems) -> newItems),
                    zipper((existingInputs, existingItems) -> existingItems),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGameFamily>> boardGameMechanicsContainer =
            familiesContainer.flatMap(
                families -> mapAndProcess(
                    families,
                    family -> {

                        BoardGameFamily boardGameFamily = new BoardGameFamily();
                        boardGameFamily.setBoardGameId(boardGameId);
                        boardGameFamily.setFamilyId(family.getId());

                        return boardGameFamily;

                    },
                    boardGameFamilyRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameMechanicsContainer;

    }

}
