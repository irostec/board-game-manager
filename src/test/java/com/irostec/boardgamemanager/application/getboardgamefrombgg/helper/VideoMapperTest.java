package com.irostec.boardgamemanager.application.getboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.shared.bggapi.output.Video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * VideoMapperTest
 */
class VideoMapperTest {

    @Test
    void roundtrip() {

        final Video source = new Video(
                    "1",
                    "Gloomhaven Review - with Tom Vasel",
                    "review",
                    "English",
                    "https://www.youtube.com/watch?v=PFzNBEOGuEQ"
            );

        final Video result = VideoMapper.INSTANCE.map(VideoMapper.INSTANCE.map(source));

        assertEquals(source, result);

    }

}
