package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output;

import com.irostec.boardgamemanager.common.type.PositiveInteger;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import java.util.Set;

/**
 * BoardGameFromBGGWithPartitionedLinks
 * A board game with the links partitioned by type
 */
@Value
@Builder
public final class BoardGameFromBGGWithPartitionedLinks {
    String externalId;
    @Singular Set<Image> images;
    @Singular Set<Name> names;
    String description;
    int yearOfPublication;
    PositiveInteger minPlayers;
    PositiveInteger maxPlayers;
    PositiveInteger minPlaytimeInMinutes;
    PositiveInteger maxPlaytimeInMinutes;
    References references;
    @Singular Set<Video> videos;
}
