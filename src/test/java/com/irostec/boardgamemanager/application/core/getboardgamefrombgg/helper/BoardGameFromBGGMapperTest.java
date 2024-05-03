package com.irostec.boardgamemanager.application.core.getboardgamefrombgg.helper;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.helper.BoardGameFromBGGMapper;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.Reference;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;

import com.irostec.boardgamemanager.TestingUtils;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.LinkType;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * BoardGameFromBGGMapperTest
 */
class BoardGameFromBGGMapperTest {

    @Test
    void testBoardGameConversion() {

        final BoardGameFromBGG source = TestingUtils.buildBoardGameFromBGG();

        final BoardGameFromBGGWithPartitionedLinks result =
                BoardGameFromBGGMapper.INSTANCE.toBoardGameFromBGGWithPartitionedLinks(source);

        assertEquals(source.id(), result.externalId());
        assertEquals(source.description(), result.description());
        assertEquals(source.yearPublished(), result.yearOfPublication());
        assertEquals(source.players().getMinimum(), result.minimumPlayers());
        assertEquals(source.players().getMaximum(), result.maximumPlayers());
        assertEquals(source.playtime().getMinimum(), result.minimumPlaytimeInMinutes());
        assertEquals(source.playtime().getMaximum(), result.maximumPlaytimeInMinutes());
        assertEquals(source.playtime().getAverage(), result.averagePlaytimeInMinutes());
        assertEquals(source.minAge(), result.minAge());
        assertEquals(source.links(), buildLinks(result));
        assertEquals(source.averageRating(), result.averageRating());
    }

    private static Set<Link> buildLinks(BoardGameFromBGGWithPartitionedLinks source) {

        final BiFunction<Set<Reference>, LinkType, Stream<Link>> referencesToLinks = (references, linkType) ->
                references.stream().map(reference -> new Link(linkType, reference.externalId(), reference.name()));

        return Streams.concat(
                referencesToLinks.apply(source.references().categories(), LinkType.CATEGORY),
                referencesToLinks.apply(source.references().mechanics(), LinkType.MECHANIC),
                referencesToLinks.apply(source.references().families(), LinkType.FAMILY),
                referencesToLinks.apply(source.references().expansions(), LinkType.EXPANSION),
                referencesToLinks.apply(source.references().accessories(), LinkType.ACCESSORY),
                referencesToLinks.apply(source.references().integrations(), LinkType.INTEGRATION),
                referencesToLinks.apply(source.references().implementations(), LinkType.IMPLEMENTATION),
                referencesToLinks.apply(source.references().designers(), LinkType.DESIGNER),
                referencesToLinks.apply(source.references().artists(), LinkType.ARTIST),
                referencesToLinks.apply(source.references().publishers(), LinkType.PUBLISHER)
        )
        .collect(Collectors.toSet());

    }

}
