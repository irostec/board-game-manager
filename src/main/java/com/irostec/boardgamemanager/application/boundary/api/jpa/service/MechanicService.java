package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameMechanicRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.MechanicRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameMechanicCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.MechanicCollectionFilter;
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
public class MechanicService {

    private final MechanicRepository mechanicRepository;
    private final BoardGameMechanicRepository boardGameMechanicRepository;
    private final BoardGameMechanicCollectionFilter boardGameMechanicCollectionFilter;
    private final MechanicCollectionFilter mechanicCollectionFilter;

    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<Mechanic> saveMechanics(DataSource dataSource, Collection<Link> links) {

        EntityCollectionProcessor.Result<Link, Mechanic> filteringResult =
            entityCollectionProcessor.apply(
                links, dataSource, mechanicCollectionFilter, Link::id, Mechanic::getExternalId
            );

        BiConsumer<Link, Mechanic> mechanicConsumer = (link, mechanic) -> {

            mechanic.setDataSource(dataSource);
            mechanic.setExternalId(link.id());
            mechanic.setName(link.value());

        };

        Collection<Mechanic> newMechanics = mechanicRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    link -> {

                        Mechanic newMechanic = new Mechanic();
                        mechanicConsumer.accept(link, newMechanic);

                        return newMechanic;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(mechanicConsumer);

        return Stream.concat(newMechanics.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    private Collection<BoardGameMechanic> saveBoardGameMechanics(
        BoardGame boardGame,
        Collection<Mechanic> mechanics
    ) {

        EntityCollectionProcessor.Result<Mechanic, BoardGameMechanic> filteringResult =
            entityCollectionProcessor.apply(
                mechanics, boardGame, boardGameMechanicCollectionFilter, Function.identity(), BoardGameMechanic::getMechanic
            );

        BiConsumer<Mechanic, BoardGameMechanic> mechanicConsumer =
            (mechanic, boardGameMechanic) -> {

                boardGameMechanic.setBoardGame(boardGame);
                boardGameMechanic.setMechanic(mechanic);

            };

        Collection<BoardGameMechanic> newBoardGameMechanics = boardGameMechanicRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    mechanic -> {

                        BoardGameMechanic newBoardGameMechanic = new BoardGameMechanic();
                        mechanicConsumer.accept(mechanic, newBoardGameMechanic);

                        return newBoardGameMechanic;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(mechanicConsumer);

        return Stream.concat(newBoardGameMechanics.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    @Transactional
    public Collection<BoardGameMechanic> saveBoardGameMechanics(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<Mechanic> mechanics = this.saveMechanics(dataSource, links);

        return saveBoardGameMechanics(boardGame, mechanics);

    }

}
