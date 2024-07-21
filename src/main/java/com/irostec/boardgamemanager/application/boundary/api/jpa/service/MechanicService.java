package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameMechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Mechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameMechanicRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.MechanicRepository;
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
public class MechanicService {

    private final MechanicRepository mechanicRepository;
    private final BoardGameMechanicRepository boardGameMechanicRepository;

    public <E> Either<E, Collection<BoardGameMechanic>> saveMechanics(
        Long boardGameId,
        Long dataSourceId,
        Supplier<Collection<Link>> linksSupplier,
        Function<Link, String> linkToKey,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, Mechanic>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> mechanicRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                Mechanic::getExternalId
            );

        Either<E, Collection<Mechanic>> mechanicsContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        link -> {

                            Mechanic mechanic = new Mechanic();
                            mechanic.setDataSourceId(dataSourceId);
                            mechanic.setExternalId(link.id());
                            mechanic.setName(link.value());

                            return mechanic;

                        },
                        mechanicRepository::saveAll,
                        exceptionToError
                    ),
                    Either::right,
                    zipper((newInputs, newItems) -> newItems),
                    zipper((existingInputs, existingItems) -> existingItems),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGameMechanic>> boardGameMechanicsContainer =
            mechanicsContainer.flatMap(
                mechanics -> mapAndProcess(
                    mechanics,
                    mechanic -> {

                        BoardGameMechanic boardGameMechanic = new BoardGameMechanic();
                        boardGameMechanic.setBoardGameId(boardGameId);
                        boardGameMechanic.setMechanicId(mechanic.getId());

                        return boardGameMechanic;

                    },
                    boardGameMechanicRepository::saveAll,
                        exceptionToError
                )
            );

        return boardGameMechanicsContainer;

    }

}
