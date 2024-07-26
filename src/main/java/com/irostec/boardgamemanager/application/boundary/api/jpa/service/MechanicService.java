package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameMechanic;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Mechanic;
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
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class MechanicService {

    private final MechanicRepository mechanicRepository;
    private final BoardGameMechanicRepository boardGameMechanicRepository;
    private final BoardGameMechanicCollectionFilter boardGameMechanicCollectionFilter;
    private final MechanicCollectionFilter mechanicCollectionFilter;

    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<Mechanic> saveMechanics(Long dataSourceId, Collection<Link> links)
    throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, Mechanic> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links, dataSourceId, mechanicCollectionFilter, Link::id, Mechanic::getExternalId
            );

        Collection<Mechanic> newMechanics = wrapWithErrorHandling(() ->
            mechanicRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        link -> {

                            Mechanic mechanic = new Mechanic();
                            mechanic.setDataSourceId(dataSourceId);
                            mechanic.setExternalId(link.id());
                            mechanic.setName(link.value());

                            return mechanic;

                        }
                    )
                    .toList()
            )
        );

        Collection<Mechanic> existingMechanics = partialFunctionDefinition.getImage();

        return Stream.concat(newMechanics.stream(), existingMechanics.stream()).toList();

    }

    private Collection<BoardGameMechanic> createBoardGameMechanics(Long boardGameId, Collection<Mechanic> mechanics)
    throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Mechanic, BoardGameMechanic> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                mechanics, boardGameId, boardGameMechanicCollectionFilter, Mechanic::getId, BoardGameMechanic::getMechanicId
            );

        Collection<BoardGameMechanic> newBoardGameMechanics = wrapWithErrorHandling(() ->
            boardGameMechanicRepository.saveAll(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        mechanic -> {

                            BoardGameMechanic boardGameMechanic = new BoardGameMechanic();
                            boardGameMechanic.setBoardGameId(boardGameId);
                            boardGameMechanic.setMechanicId(mechanic.getId());

                            return boardGameMechanic;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameMechanic> existingBoardGameMechanics = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameMechanics.stream(), existingBoardGameMechanics.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameMechanic> saveBoardGameMechanics(
        Long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    )
    throws BoundaryException {

        Collection<Mechanic> mechanics = this.saveMechanics(dataSourceId, links);

        return createBoardGameMechanics(boardGameId, mechanics);

    }

}
