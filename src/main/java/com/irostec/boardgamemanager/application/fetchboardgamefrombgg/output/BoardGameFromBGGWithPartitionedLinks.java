package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output;

import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeInteger;
import com.irostec.boardgamemanager.common.type.PositiveInteger;
import java.util.Set;

/**
 * BoardGameFromBGGWithPartitionedLinks
 * A board game with the links partitioned by type
 */
public record BoardGameFromBGGWithPartitionedLinks (
    String externalId,
    Set<Image> images,
    Set<Name> names,
    String description,
    int yearOfPublication,
    PositiveInteger minPlayers,
    PositiveInteger maxPlayers,
    PositiveInteger playingTimeInMinutes,
    PositiveInteger minPlaytimeInMinutes,
    PositiveInteger maxPlaytimeInMinutes,
    NonNegativeInteger minAge,
    References references,
    Set<Video> videos,
    NonNegativeDecimal averageRating
) {}
