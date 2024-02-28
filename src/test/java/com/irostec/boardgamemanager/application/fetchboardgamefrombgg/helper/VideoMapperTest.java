package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * VideoMapperTest
 */
class VideoMapperTest {

    @Test
    void mapVideo() {


        final com.irostec.boardgamemanager.application.shared.bggapi.output.Video source =
            new com.irostec.boardgamemanager.application.shared.bggapi.output.Video(
                    "1",
                    "Gloomhaven Review - with Tom Vasel",
                    "review",
                    "English",
                    "https://www.youtube.com/watch?v=PFzNBEOGuEQ"
            );

        final com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.Video result =
                VideoMapper.INSTANCE.mapVideo(source);

        assertEquals(source.id(), result.externalId());
        assertEquals(source.title(), result.title());
        assertEquals(source.category(), result.category());
        assertEquals(source.language(), result.language());
        assertEquals(source.link(), result.link());

    }

}
