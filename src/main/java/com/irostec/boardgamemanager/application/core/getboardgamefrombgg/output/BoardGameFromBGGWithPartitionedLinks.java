package com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output;

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
    PositiveInteger minimumPlayers,
    PositiveInteger maximumPlayers,
    PositiveInteger averagePlaytimeInMinutes,
    PositiveInteger minimumPlaytimeInMinutes,
    PositiveInteger maximumPlaytimeInMinutes,
    NonNegativeInteger minAge,
    References references,
    Set<Video> videos,
    NonNegativeDecimal averageRating
) {}
