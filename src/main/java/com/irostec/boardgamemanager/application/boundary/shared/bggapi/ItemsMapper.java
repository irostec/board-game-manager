package com.irostec.boardgamemanager.application.boundary.shared.bggapi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.*;
import com.irostec.boardgamemanager.application.boundary.shared.bggapi.jaxb.generated.Items;
import com.irostec.boardgamemanager.common.error.ErrorUtils;
import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;

import com.google.common.collect.ImmutableList;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.PositiveInteger;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.InvalidBoardGameData;

/**
 * ItemsMapper
 * Transforms an Items instance into the corresponding BoardGameFromBGG instances
 */
final class ItemsMapper {

    private ItemsMapper() {};

    public static Validation<InvalidBoardGameData, BoardGameFromBGG> itemToBoardGame(Items.Item item) {

        final List<Image> images = List.of(
                new Image(ImageType.THUMBNAIL, item.getThumbnail()),
                new Image(ImageType.IMAGE, item.getImage())
        );

        final Validation<ErrorSummary, List<Name>> namesValidations = ErrorUtils.sequence(
                item
                        .getName()
                        .stream()
                        .map(
                                name -> NameType
                                        .fromName(name.getType())
                                        .map(nameType -> new Name(nameType, name.getVal()))
                        )
        )
        .mapError(ErrorSummary.CURRIED_MULTI_MESSAGE_BUILDER.apply("names"));

        final String externalId = item.getId();
        final String description = item.getDescription();
        final int yearPublished = item.getYearpublished().getVal();

        final Validation<String, PositiveInteger> minimumPlayersValidation = PositiveInteger.of("minimumPlayers", item.getMinplayers().getVal());
        final Validation<String, PositiveInteger> maximumPlayersValidation = PositiveInteger.of("maximumPlayers", item.getMaxplayers().getVal());
        final Validation<ErrorSummary, Players> playersValidation =
                Validation.combine(minimumPlayersValidation, maximumPlayersValidation)
                        .ap(Players::of)
                        .flatMap(validation -> validation.mapError(io.vavr.collection.List::of))
                        .mapError(ErrorSummary.CURRIED_MULTI_MESSAGE_BUILDER.apply("players"));

        final Validation<String, PositiveInteger> minimumPlaytimeValidation = PositiveInteger.of("minimumPlaytime", item.getMinplaytime().getVal());
        final Validation<String, PositiveInteger> maximumPlaytimeValidation = PositiveInteger.of("maximumPlaytime", item.getMaxplaytime().getVal());
        final Validation<String, PositiveInteger> averagePlaytimeValidation = PositiveInteger.of("averagePlaytime", item.getPlayingtime().getVal());
        final Validation<ErrorSummary, Playtime> playtimeValidation =
                Validation.combine(minimumPlaytimeValidation, maximumPlaytimeValidation, averagePlaytimeValidation)
                        .ap(Playtime::of)
                        .flatMap(validation -> validation.mapError(io.vavr.collection.List::of))
                        .mapError(ErrorSummary.CURRIED_MULTI_MESSAGE_BUILDER.apply("players"));

        final String minimumAgePropertyName = "minimumAge";
        final Validation<ErrorSummary, NonNegativeInteger> minimumAgeValidation =
                NonNegativeInteger.of(minimumAgePropertyName, item.getMinage().getVal())
                        .mapError(ErrorSummary.CURRIED_SINGLE_MESSAGE_BUILDER.apply(minimumAgePropertyName));

        final Validation<ErrorSummary, ImmutableSet<Link>> linksValidations = ErrorUtils.sequence(
                item
                        .getLink()
                        .stream()
                        .map(link -> LinkType
                                .fromPrefixedName(link.getType())
                                .map(linkType -> new Link(linkType, link.getId(), link.getVal()))
                        )
        )
        .map(ImmutableSet::copyOf)
        .mapError(ErrorSummary.CURRIED_MULTI_MESSAGE_BUILDER.apply("links"));;

        final List<Video> videos = item.getVideos().getVideo().stream()
                .map(video -> new Video(video.getId(), video.getTitle(), video.getCategory(), video.getLanguage(), video.getLink()))
                .collect(ImmutableList.toImmutableList());

        final String averageRatingPropertyName = "averageRating";
        final Validation<ErrorSummary, NonNegativeDecimal> averageRatingValidation =
                NonNegativeDecimal.of(
                        averageRatingPropertyName,
                        item.getStatistics().getRatings().getAverage().getVal()
                )
                .mapError(ErrorSummary.CURRIED_SINGLE_MESSAGE_BUILDER.apply(averageRatingPropertyName));

        return Validation.combine(
                namesValidations,
                playersValidation,
                playtimeValidation,
                minimumAgeValidation,
                linksValidations,
                averageRatingValidation
        )
        .ap(
                (names, players, playtime, minimumAge, links, averageRating) ->
                        new BoardGameFromBGG(
                                externalId,
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
                        )
        )
        .mapError(errorSummaries ->
                        errorSummaries.toJavaStream().collect(
                                Collectors.groupingBy(
                                        ErrorSummary::getPropertyName,
                                        Collectors.<ErrorSummary, Iterable<String>>reducing(
                                                Collections.emptyList(),
                                                ErrorSummary::getErrorMessages,
                                                (iterable1, iterable2) -> Iterables.concat(iterable1, iterable2)
                                        )
                                )
                        )
        )
        .mapError(errorsByPropertyName -> new InvalidBoardGameData(externalId, errorsByPropertyName));

    }

    @AllArgsConstructor
    @Getter
    private static class ErrorSummary {

        private static final Function<String, Function<String, ErrorSummary>> CURRIED_SINGLE_MESSAGE_BUILDER =
                propertyName -> errorMessage -> new ErrorSummary(propertyName, errorMessage);

        private static final Function<String, Function<Iterable<String>, ErrorSummary>> CURRIED_MULTI_MESSAGE_BUILDER =
                propertyName -> errorMessages -> new ErrorSummary(propertyName, errorMessages);

        private final String propertyName;
        private final Iterable<String> errorMessages;

        private ErrorSummary(String propertyName, String errorMessage) {
            this(propertyName, List.of(errorMessage));
        }

    }

}
