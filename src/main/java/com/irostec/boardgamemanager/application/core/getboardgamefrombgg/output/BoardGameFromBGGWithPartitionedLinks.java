package com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output;

import com.irostec.boardgamemanager.common.type.NonNegativeDecimal;
import com.irostec.boardgamemanager.common.type.NonNegativeShort;
import com.irostec.boardgamemanager.common.type.PositiveShort;

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
    short yearOfPublication,
    PositiveShort minimumPlayers,
    PositiveShort maximumPlayers,
    PositiveShort averagePlaytimeInMinutes,
    PositiveShort minimumPlaytimeInMinutes,
    PositiveShort maximumPlaytimeInMinutes,
    NonNegativeShort minAge,
    References references,
    Set<Video> videos,
    NonNegativeDecimal averageRating
) {}
