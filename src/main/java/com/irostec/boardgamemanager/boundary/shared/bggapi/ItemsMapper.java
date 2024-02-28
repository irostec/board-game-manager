package com.irostec.boardgamemanager.boundary.shared.bggapi;

import com.irostec.boardgamemanager.application.shared.bggapi.output.*;
import com.irostec.boardgamemanager.boundary.shared.bggapi.jaxb.generated.Items;
import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;

import com.google.common.collect.ImmutableList;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.PositiveInteger;

import java.util.List;

import static com.irostec.boardgamemanager.common.utility.ExceptionUtils.mapToList;
import static com.irostec.boardgamemanager.common.utility.ExceptionUtils.lift;

/**
 * ItemsMapper
 * Transforms an Items instance into the corresponding BoardGameFromBGG instances
 */
final class ItemsMapper {

    private ItemsMapper() {};

    public static BoardGameFromBGG itemToBoardGame(Items.Item item) throws BGMException {

        final List<Image> images = List.of(
                new Image(ImageType.THUMBNAIL, item.getThumbnail()),
                new Image(ImageType.IMAGE, item.getImage())
        );

        final List<Name> names = mapToList(
            item.getName().stream(),
            lift(name -> new Name(NameType.of(name.getType()), name.getVal()))
        );

        final String id = item.getId();
        final String description = item.getDescription();
        final int yearPublished = item.getYearpublished().getVal();
        final PositiveInteger minPlayers = new PositiveInteger(item.getMinplayers().getVal());
        final PositiveInteger maxPlayers = new PositiveInteger(item.getMaxplayers().getVal());
        final PositiveInteger playingTime = new PositiveInteger(item.getPlayingtime().getVal());
        final PositiveInteger minPlaytime = new PositiveInteger(item.getMinplaytime().getVal());
        final PositiveInteger maxPlaytime = new PositiveInteger(item.getMaxplaytime().getVal());
        final NonNegativeInteger minAge = new NonNegativeInteger(item.getMinage().getVal());

        final List<Link> links = mapToList(
            item.getLink().stream(),
            lift(link -> new Link(LinkType.of(link.getType()), link.getId(), link.getVal()))
        );

        final List<Video> videos = item.getVideos().getVideo().stream()
                .map(video -> new Video(video.getId(), video.getTitle(), video.getCategory(), video.getLanguage(), video.getLink()))
                .collect(ImmutableList.toImmutableList());

        final NonNegativeDecimal averageRating = new NonNegativeDecimal(item.getStatistics().getRatings().getAverage().getVal());

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

    public static List<BoardGameFromBGG> itemsToBoardGames(Items items) throws BGMException {

        return mapToList(
            items.getItem().stream(),
            lift(ItemsMapper::itemToBoardGame)
        );

    }

}
