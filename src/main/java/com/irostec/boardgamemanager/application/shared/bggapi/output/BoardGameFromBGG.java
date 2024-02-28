package com.irostec.boardgamemanager.application.shared.bggapi.output;

import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.PositiveInteger;

import java.util.List;

/**
 * BoardGameFromBGG
 * A board game, as represented in boardgamegeek.com
 */
public record BoardGameFromBGG (String id,
                                List<Image> images,
                                List<Name> names,
                                String description,
                                int yearPublished,
                                PositiveInteger minPlayers,
                                PositiveInteger maxPlayers,
                                PositiveInteger playingTime,
                                PositiveInteger minPlaytime,
                                PositiveInteger maxPlaytime,
                                NonNegativeInteger minAge,
                                List<Link> links,
                                List<Video> videos,
                                NonNegativeDecimal averageRating

) {}
