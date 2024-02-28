package com.irostec.boardgamemanager.application;

import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.application.shared.bggapi.output.*;
import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.exception.NotFoundException;
import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.PositiveInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * FetchBoardGameFromBGGTest
 * Tests for the FetchBoardGameFromBGG use case
 */
class FetchBoardGameFromBGGTest {

    private static final String ID = "174430";

    private FetchBoardGameFromBGG fetchBoardGameFromBGG;

    @BeforeEach
    void init() throws BGMException {

        final BGGApi bggApi = externalId -> {
                if (ID.equals(externalId)) {
                    return buildBoardGameFromBGG();
                } else {
                    throw new NotFoundException(String.format("No board game with id '%s' was found in boardgamegeek.com", externalId));
                }
        };

        fetchBoardGameFromBGG = new FetchBoardGameFromBGG(bggApi);

    }

    @Test
    void givenAFetchBoardGameFromBGGUseCase_whenItIsExecutedWithAnUnknownId_thenANotFoundExceptionShouldBeThrown() {

        final String unknownId = "someRandomId";

        assertThrows(NotFoundException.class, () -> fetchBoardGameFromBGG.execute(unknownId));

    }

    @Test
    void givenAFetchBoardGameFromBGGUseCase_whenItIsExecutedWithAKnownId_thenABoardGameFromBGGWithPartitionedLinksShouldBeReturned() throws BGMException {

        final BoardGameFromBGGWithPartitionedLinks result =
            fetchBoardGameFromBGG.execute(ID);

        assertEquals(ID, result.externalId());


    }

    private static BoardGameFromBGG buildBoardGameFromBGG() throws BGMException {

        final String id = "174430";

        final List<Image> images = List.of(
                new Image(
                        ImageType.THUMBNAIL,
                "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__thumb/img/veqFeP4d_3zNhFc3GNBkV95rBEQ=/fit-in/200x150/filters:strip_icc()/pic2437871.jpg"
                ),
                new Image(
                        ImageType.IMAGE,
                        "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__original/img/7d-lj5Gd1e8PFnD97LYFah2c45M=/0x0/filters:format(jpeg)/pic2437871.jpg"
                )
        );

        final List<Name> names = List.of(new Name(NameType.PRIMARY, "Gloomhaven"));

        final String description = "Gloomhaven is a game of Euro-inspired tactical combat in a persistent world of shifting motives.";

        final int yearPublished = 2017;

        final PositiveInteger minPlayers = new PositiveInteger(1);
        final PositiveInteger maxPlayers = new PositiveInteger(4);
        final PositiveInteger playingTime = new PositiveInteger(120);
        final PositiveInteger minPlaytime = new PositiveInteger(60);
        final PositiveInteger maxPlaytime = new PositiveInteger(120);
        final NonNegativeInteger minAge = new NonNegativeInteger(14);

        final List<Link> links = List.of(
            new Link(LinkType.CATEGORY, "1022", "Adventure"),
            new Link(LinkType.CATEGORY, "1020", "Exploration"),
            new Link(LinkType.CATEGORY, "1010", "Fantasy"),
            new Link(LinkType.CATEGORY, "1046", "Fighting"),
            new Link(LinkType.MECHANIC, "2023", "Cooperative Game"),
            new Link(LinkType.MECHANIC, "2028", "Role Playing"),
            new Link(LinkType.MECHANIC, "2846", "Once-Per-Game Abilities"),
            new Link(LinkType.FAMILY, "24281", "Mechanism: Campaign Games"),
            new Link(LinkType.EXPANSION, "365186", "The Crimson Scales"),
            new Link(LinkType.EXPANSION, "226868", "Gloomhaven: Solo Scenarios"),
            new Link(LinkType.ACCESSORY, "250858", "Gloomhaven: Miniatures"),
            new Link(LinkType.INTEGRATION, "295770", "Frosthaven"),
            new Link(LinkType.INTEGRATION, "291457", "Gloomhaven: Jaws of the Lion"),
            new Link(LinkType.IMPLEMENTATION, "390478", "Gloomhaven: Second Edition"),
            new Link(LinkType.DESIGNER, "69802", "Isaac Childres"),
            new Link(LinkType.ARTIST, "77084", "Alexandr Elichev"),
            new Link(LinkType.ARTIST, "78961", "Josh T. McDowell"),
            new Link(LinkType.ARTIST, "84269", "Alvaro Nebot"),
            new Link(LinkType.PUBLISHER, "27425", "Cephalofair Games")
        );

        final List<Video> videos = List.of(
                new Video(
                        "1",
                        "Gloomhaven Review - with Tom Vasel",
                        "review",
                        "English",
                        "https://www.youtube.com/watch?v=PFzNBEOGuEQ"
                )
        );

        final NonNegativeDecimal averageRating = new NonNegativeDecimal(8.59883f);

        return new BoardGameFromBGG(
                id,
                images,
                names,
                description,
                yearPublished,
                minPlayers,
                maxPlayers,
                playingTime,
                minPlaytime,
                maxPlaytime,
                minAge,
                links,
                videos,
                averageRating
        );

    }

}
