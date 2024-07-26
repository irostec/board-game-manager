package com.irostec.boardgamemanager.application.boundary.shared.createboardgamefrombgg.dependency;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.service.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.LinkType;

import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGException;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.MissingPropertyException;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.BoundaryException;

import com.irostec.boardgamemanager.common.error.BGMException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
final class SaveBGGBoardGameFixedDataInJPA implements SaveBGGBoardGameFixedData {

    private final Logger logger = LogManager.getLogger(SaveBGGBoardGameFixedDataInJPA.class);

    private final DataSourceService dataSourceService;
    private final BoardGameService boardGameService;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final MechanicService mechanicService;
    private final FamilyService familyService;
    private final ExpansionService expansionService;
    private final DesignerService designerService;
    private final ArtistService artistService;
    private final PublisherService publisherService;
    private final AccessoryService accessoryService;
    private final IntegrationService integrationService;
    private final ImplementationService implementationService;

    @Override
    public BoardGameSummary execute(BoardGameFromBGG boardGameFromBGG)
        throws CreateBoardGameFromBGGException {

        try {

            long dataSourceId = dataSourceService.getDataSource(DataSourceName.BOARD_GAME_GEEK).getId();

            logger.info(
                String.format(
                    "Creating board game with the external id '%s' using data from boardgamegeek.com",
                    boardGameFromBGG.id()
                )
            );

            ImmutablePair<BoardGame, BoardGameReference> boardGameWithReference =
                boardGameService.saveBoardGame(dataSourceId, boardGameFromBGG);

            BoardGameSummary result = processBoardGame(
                boardGameFromBGG,
                dataSourceId,
                boardGameWithReference.getLeft(),
                boardGameWithReference.getRight()
            );

            logger.info(String.format("Created board game with id '%d'", result.id()));

            return result;

        }
        catch (BGMException bgmException) {
            String message = String.format("Error creating board game with the external id '%s' using data from boardgamegeek.com", boardGameFromBGG.id());
            logger.error(message, bgmException);

            throw switch (bgmException) {
                case com.irostec.boardgamemanager.common.error.RequiredValueNotFoundException requiredValueNotFoundException ->
                    new MissingPropertyException(requiredValueNotFoundException);
                case com.irostec.boardgamemanager.common.error.BoundaryException boundaryException ->
                    new BoundaryException(boundaryException);
            };

        }

    }

    private BoardGameSummary processBoardGame(
        BoardGameFromBGG boardGameFromBGG,
        Long dataSourceId,
        BoardGame boardGame,
        BoardGameReference boardGameReference
    ) throws BGMException {

        long boardGameId = boardGame.getId();
        long boardGameReferenceId = boardGameReference.getId();

        BoardGameFixedData boardGameFixedData =
            boardGameService.saveBoardGameFixedData(boardGameId, boardGameFromBGG);

        Collection<com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image> images =
            imageService.saveImages(boardGameReferenceId, boardGameFromBGG.images());


        Map<LinkType, List<Link>> linksByLinkType = boardGameFromBGG.links().stream().collect(
            Collectors.groupingBy(
                Link::type,
                Collectors.toList()
            )
        );
        List<Link> defaultValue = Collections.emptyList();


        Collection<BoardGameCategory> categories =
            categoryService.saveBoardGameCategories(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.CATEGORY, defaultValue)
            );

        Collection<BoardGameMechanic> mechanics =
            mechanicService.saveBoardGameMechanics(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.MECHANIC, defaultValue)
            );

        Collection<BoardGameFamily> families =
            familyService.saveBoardGameFamilies(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.FAMILY, defaultValue)
            );


        Collection<BoardGameExpansion> expansions =
            expansionService.saveBoardGameExpansions(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.EXPANSION, defaultValue)
            );

        Collection<BoardGameAccessory> accessories =
            accessoryService.saveBoardGameAccessories(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.ACCESSORY, defaultValue)
            );

        Collection<BoardGameIntegration> integrations =
            integrationService.saveBoardGameIntegrations(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.INTEGRATION, defaultValue)
            );

        Collection<BoardGameImplementation> implementations =
            implementationService.saveBoardGameImplementations(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.IMPLEMENTATION, defaultValue)
            );


        Collection<BoardGameDesigner> designers =
            designerService.saveBoardGameDesigners(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.DESIGNER, defaultValue)
            );

        Collection<BoardGameArtist> artists =
            artistService.saveBoardGameArtists(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.ARTIST, defaultValue)
            );

        Collection<BoardGamePublisher> publishers =
            publisherService.saveBoardGamePublishers(
                boardGameId,
                dataSourceId,
                linksByLinkType.getOrDefault(LinkType.PUBLISHER, defaultValue)
            );

        return new BoardGameSummary(boardGameId);

    }

}
