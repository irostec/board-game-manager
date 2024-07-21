package com.irostec.boardgamemanager.application.boundary.getboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.Image;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.Name;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.Reference;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.Video;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import static com.irostec.boardgamemanager.common.utility.Mapping.toListOfMaps;

/**
 * OutputMapper
 * Proof of concept for a controller returning a java.util.Map instead of a DTO,
 * thus avoiding the creation of the latter and also of the corresponding mapper.
 * This could become a static method inside the controller, but it's preferred to keep the concerns separated.
 */
final class OutputMapper {

    private OutputMapper() {}

    private static final Function<Collection<Reference>, List<Map<String, String>>> REFERENCES_CONVERTER =
            references -> toListOfMaps(
                    references,
                    ImmutablePair.of(reference -> "externalId", Reference::externalId),
                    ImmutablePair.of(reference -> "name", Reference::name)
            );

    static Map<String, Object> toMap(BoardGameFromBGGWithPartitionedLinks boardGame) {

        final List<Map<String, String>> images = toListOfMaps(
                boardGame.images(),
                ImmutablePair.of(image -> "type", Image::type),
                ImmutablePair.of(image -> "url", Image::url)
        );

        final List<Map<String, String>> names = toListOfMaps(
                boardGame.names(),
                ImmutablePair.of(name -> "type", Name::type),
                ImmutablePair.of(name -> "value", Name::value)
        );

        final List<Map<String, String>> videos = toListOfMaps(
                boardGame.videos(),
                ImmutablePair.of(video -> "externalId", Video::externalId),
                ImmutablePair.of(video -> "title", Video::title),
                ImmutablePair.of(video -> "category", Video::category),
                ImmutablePair.of(video -> "language", Video::language),
                ImmutablePair.of(video -> "link", Video::link)
        );

        return ImmutableMap.<String, Object>builder()
                .put("externalId", boardGame.externalId())
                .put("images", images)
                .put("names", names)
                .put("description", boardGame.description())
                .put("yearOfPublication", boardGame.yearOfPublication())
                .put("minimumPlayers", boardGame.minimumPlayers().getValue())
                .put("maximumPlayers", boardGame.maximumPlayers().getValue())
                .put("minimumPlaytimeInMinutes", boardGame.minimumPlaytimeInMinutes().getValue())
                .put("maximumPlaytimeInMinutes", boardGame.maximumPlaytimeInMinutes().getValue())
                .put("categories", REFERENCES_CONVERTER.apply(boardGame.references().categories()))
                .put("mechanics", REFERENCES_CONVERTER.apply(boardGame.references().mechanics()))
                .put("families", REFERENCES_CONVERTER.apply(boardGame.references().families()))
                .put("expansions", REFERENCES_CONVERTER.apply(boardGame.references().expansions()))
                .put("accessories", REFERENCES_CONVERTER.apply(boardGame.references().accessories()))
                .put("integrations", REFERENCES_CONVERTER.apply(boardGame.references().integrations()))
                .put("implementations", REFERENCES_CONVERTER.apply(boardGame.references().implementations()))
                .put("designers", REFERENCES_CONVERTER.apply(boardGame.references().designers()))
                .put("artists", REFERENCES_CONVERTER.apply(boardGame.references().artists()))
                .put("publishers", REFERENCES_CONVERTER.apply(boardGame.references().publishers()))
                .put("videos", videos)
                .build();

    }

}
