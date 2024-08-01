package com.irostec.boardgamemanager.application.boundary.shared.createboardgamefrombgg.dependency;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.service.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.LinkType;

import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
class SaveBGGBoardGameFixedDataInJPA implements SaveBGGBoardGameFixedData {

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

    @Transactional
    @Override
    public BoardGameSummary execute(BoardGameFromBGG boardGameFromBGG) {

        DataSource dataSource = dataSourceService.getDataSource(DataSourceName.BOARD_GAME_GEEK);

        ImmutablePair<BoardGame, BoardGameReference> boardGameWithReference =
            boardGameService.saveBoardGame(dataSource, boardGameFromBGG);

        return processBoardGameCollections(
            boardGameFromBGG,
            dataSource,
            boardGameWithReference.getLeft(),
            boardGameWithReference.getRight()
        );

    }

    private BoardGameSummary processBoardGameCollections(
        BoardGameFromBGG boardGameFromBGG,
        DataSource dataSource,
        BoardGame boardGame,
        BoardGameReference boardGameReference
    ) {

        Collection<com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image> images =
            imageService.saveImages(boardGameReference, boardGameFromBGG.images());

        Map<LinkType, List<Link>> linksByLinkType = boardGameFromBGG.links().stream().collect(
            Collectors.groupingBy(
                Link::type,
                Collectors.toList()
            )
        );
        List<Link> defaultValue = Collections.emptyList();

        Collection<BoardGameCategory> categories =
            categoryService.saveBoardGameCategories(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.CATEGORY, defaultValue)
            );

        Collection<BoardGameMechanic> mechanics =
            mechanicService.saveBoardGameMechanics(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.MECHANIC, defaultValue)
            );

        Collection<BoardGameFamily> families =
            familyService.saveBoardGameFamilies(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.FAMILY, defaultValue)
            );


        Collection<BoardGameExpansion> expansions =
            expansionService.saveBoardGameExpansions(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.EXPANSION, defaultValue)
            );

        Collection<BoardGameAccessory> accessories =
            accessoryService.saveBoardGameAccessories(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.ACCESSORY, defaultValue)
            );

        Collection<BoardGameIntegration> integrations =
            integrationService.saveBoardGameIntegrations(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.INTEGRATION, defaultValue)
            );

        Collection<BoardGameImplementation> implementations =
            implementationService.saveBoardGameImplementations(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.IMPLEMENTATION, defaultValue)
            );


        Collection<BoardGameDesigner> designers =
            designerService.saveBoardGameDesigners(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.DESIGNER, defaultValue)
            );

        Collection<BoardGameArtist> artists =
            artistService.saveBoardGameArtists(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.ARTIST, defaultValue)
            );

        Collection<BoardGamePublisher> publishers =
            publisherService.saveBoardGamePublishers(
                boardGame,
                dataSource,
                linksByLinkType.getOrDefault(LinkType.PUBLISHER, defaultValue)
            );

        return new BoardGameSummary(boardGame.getId());

    }

}
