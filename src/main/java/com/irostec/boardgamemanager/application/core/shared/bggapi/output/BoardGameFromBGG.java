package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.NonNegativeShort;

import java.util.List;
import java.util.Set;

/**
 * BoardGameFromBGG
 * A board game, as represented in boardgamegeek.com
 */
public record BoardGameFromBGG (String id,
                                List<Image> images,
                                List<Name> names,
                                String description,
                                short yearPublished,
                                Players players,
                                Playtime playtime,
                                NonNegativeShort minAge,
                                Set<Link> links,
                                List<Video> videos,
                                NonNegativeDecimal averageRating

) {}
