package com.irostec.boardgamemanager.application.boundary.shared.createboardgamefrombgg.dependency;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.service.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.LinkType;

import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGError;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.DatabaseError;

import io.vavr.control.Either;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
final class SaveBGGBoardGameFixedDataInJPA implements SaveBGGBoardGameFixedData {

    private static final Function<Link, String> LINK_TO_KEY = Link::id;
    private static final Function<Throwable, CreateBoardGameFromBGGError> EXCEPTION_TO_ERROR = DatabaseError::new;

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

    SaveBGGBoardGameFixedDataInJPA(
        DataSourceService dataSourceService,
        BoardGameService boardGameService,
        ImageService imageService,
        CategoryService categoryService,
        MechanicService mechanicService,
        FamilyService familyService,
        ExpansionService expansionService,
        DesignerService designerService,
        ArtistService artistService,
        PublisherService publisherService,
        AccessoryService accessoryService,
        IntegrationService integrationService,
        ImplementationService implementationService
    ) {

        this.dataSourceService = dataSourceService;
        this.boardGameService = boardGameService;
        this.imageService = imageService;
        this.categoryService = categoryService;
        this.mechanicService = mechanicService;
        this.familyService = familyService;
        this.expansionService = expansionService;
        this.designerService = designerService;
        this.artistService = artistService;
        this.publisherService = publisherService;
        this.accessoryService = accessoryService;
        this.integrationService = integrationService;
        this.implementationService = implementationService;

    }

    @Override
    public Either<CreateBoardGameFromBGGError, BoardGameSummary> execute(BoardGameFromBGG boardGameFromBGG) {

        Either<CreateBoardGameFromBGGError, Long> dataSourceIdContainer =
            dataSourceService.getDataSource(DataSourceName.BOARD_GAME_GEEK, EXCEPTION_TO_ERROR)
            .map(DataSource::getId);

        logger.info(
            String.format(
                "Creating board game with the external id '%s' using data from boardgamegeek.com",
                boardGameFromBGG.id()
            )
        );

        Either<CreateBoardGameFromBGGError, BoardGameSummary> result =
            dataSourceIdContainer.flatMap(dataSourceId ->
                boardGameService.saveBoardGame(dataSourceId, boardGameFromBGG, EXCEPTION_TO_ERROR)
                    .flatMap(boardGameWithReference ->
                        processBoardGame(
                            boardGameFromBGG,
                            dataSourceId,
                            boardGameWithReference.getLeft(),
                            boardGameWithReference.getRight()
                        )
                    )
            );

        result
            .peek(boardGameSummary -> logger.info(String.format("Created board game with id '%d'", boardGameSummary.id())))
            .peekLeft(error -> logger.info(String.format("Error creating board game with the external id '%s' using data from boardgamegeek.com", boardGameFromBGG.id())));

        return result;

    }

    private Either<CreateBoardGameFromBGGError, BoardGameSummary> processBoardGame(
        BoardGameFromBGG boardGameFromBGG,
        Long dataSourceId,
        BoardGame boardGame,
        BoardGameReference boardGameReference
    ) {

        long boardGameId = boardGame.getId();
        long boardGameReferenceId = boardGameReference.getId();

        Either<CreateBoardGameFromBGGError, BoardGameFixedData> boardGameFixedDataContainer =
            boardGameService.saveBoardGameFixedData(boardGameId, boardGameFromBGG, EXCEPTION_TO_ERROR);

        Either<CreateBoardGameFromBGGError, Collection<com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image>> imagesContainer =
            imageService.saveImages(boardGameReferenceId, boardGameFromBGG::images, EXCEPTION_TO_ERROR);


        Function<LinkType, List<Link>> linkTypeToLinks = buildFunctionToGetLinksByType(boardGameFromBGG.links());

        Either<CreateBoardGameFromBGGError, Collection<BoardGameCategory>> categoriesContainer =
            categoryService.saveCategories(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.CATEGORY),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameMechanic>> mechanicsContainer =
            mechanicService.saveMechanics(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.MECHANIC),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameFamily>> familiesContainer =
            familyService.saveFamilies(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.FAMILY),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );


        Either<CreateBoardGameFromBGGError, Collection<BoardGame>> boardGamesContainer =
            boardGameService.getAllBoardGames(EXCEPTION_TO_ERROR);

        Either<CreateBoardGameFromBGGError, Collection<BoardGameReference>> boardGameReferencesContainer =
            boardGameService.getAllBoardGameReferences(EXCEPTION_TO_ERROR);

        Either<CreateBoardGameFromBGGError, Collection<BoardGameExpansion>> expansionsContainer =
            expansionService.saveBoardGameExpansions(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.EXPANSION),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameAccessory>> accessoriesContainer =
            accessoryService.saveBoardGameAccessories(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.ACCESSORY),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameIntegration>> integrationsContainer =
            integrationService.saveBoardGameIntegrations(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.INTEGRATION),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameImplementation>> implementationsContainer =
            implementationService.saveBoardGameImplementations(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.IMPLEMENTATION),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameDesigner>> designersContainer =
            designerService.saveBoardGameDesigners(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.DESIGNER),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGameArtist>> artistsContainer =
            artistService.saveBoardGameArtists(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.ARTIST),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, Collection<BoardGamePublisher>> publishersContainer =
            publisherService.saveBoardGamePublishers(
                boardGameId,
                dataSourceId,
                () -> linkTypeToLinks.apply(LinkType.PUBLISHER),
                LINK_TO_KEY,
                EXCEPTION_TO_ERROR
            );

        Either<CreateBoardGameFromBGGError, BoardGameSummary> result =
            boardGameFixedDataContainer.flatMap(
                boardGameFixed -> imagesContainer.flatMap(
                    images -> categoriesContainer.flatMap(
                        categories -> mechanicsContainer.flatMap(
                            mechanics -> familiesContainer.flatMap(
                                families -> expansionsContainer.flatMap(
                                    expansions -> accessoriesContainer.flatMap(
                                        accessories -> integrationsContainer.flatMap(
                                            integrations -> implementationsContainer.flatMap(
                                                implementations -> designersContainer.flatMap(
                                                    designers -> artistsContainer.flatMap(
                                                        artists -> publishersContainer.map(
                                                            publishers -> new BoardGameSummary(boardGameId)
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            );

        return result;

    }

    private static Function<LinkType, List<Link>> buildFunctionToGetLinksByType(Collection<Link> links) {

        Map<LinkType, List<Link>> linksByType = links.stream().collect(Collectors.groupingBy(Link::type));

        List<Link> emptyList = Collections.emptyList();

        return
            linkType -> linksByType.getOrDefault(linkType, emptyList);

    }

}
