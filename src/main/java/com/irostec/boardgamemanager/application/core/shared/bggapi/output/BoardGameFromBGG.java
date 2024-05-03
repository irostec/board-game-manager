package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;

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
                                int yearPublished,
                                Players players,
                                Playtime playtime,
                                NonNegativeInteger minAge,
                                Set<Link> links,
                                List<Video> videos,
                                NonNegativeDecimal averageRating

) {}
