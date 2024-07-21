package com.irostec.boardgamemanager;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.*;
import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.PositiveShort;

import java.util.List;
import java.util.Set;

/**
 * TestingUtils
 * Convenient methods for testing
 */
public final class TestingUtils {

    private TestingUtils() {}

    public static BoardGameFromBGG buildBoardGameFromBGG() {

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

        final PositiveShort minimumPlayers = PositiveShort.of("minimumPlayers", 1).get();
        final PositiveShort maximumPlayers = PositiveShort.of("maximumPlayers", 4).get();
        final Players players = Players.of(minimumPlayers, maximumPlayers).get();

        final PositiveShort minimumPlaytime = PositiveShort.of("minimumPlaytime", 60).get();
        final PositiveShort maximumPlaytime = PositiveShort.of("maximumPlaytime", 120).get();
        final PositiveShort averagePlaytime = PositiveShort.of("averagePlaytime", 120).get();
        final Playtime playtime = Playtime.of(minimumPlaytime, maximumPlaytime, averagePlaytime).get();

        final NonNegativeInteger minimumAge = NonNegativeInteger.of("minimumAge", 14).get();

        final Set<Link> links = Set.of(
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

        final NonNegativeDecimal averageRating = NonNegativeDecimal.of("averageRating", 8.59883f).get();

        return new BoardGameFromBGG(
                id,
                images,
                names,
                description,
                yearPublished,
                players,
                playtime,
                minimumAge,
                links,
                videos,
                averageRating
        );

    }

}
