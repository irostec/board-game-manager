package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.shared.bggapi.output.ImageType;
import com.irostec.boardgamemanager.common.exception.BGMException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ImageMapperTest
 */
class ImageMapperTest {

    @Test
    void mapImage() {

        final com.irostec.boardgamemanager.application.shared.bggapi.output.Image source =
                new com.irostec.boardgamemanager.application.shared.bggapi.output.Image(
                        ImageType.IMAGE,
                        "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__original/img/7d-lj5Gd1e8PFnD97LYFah2c45M=/0x0/filters:format(jpeg)/pic2437871.jpg"
                );

        final com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.Image result =
                ImageMapper.INSTANCE.mapImage(source);

        assertEquals("image", result.type());
        assertEquals(source.link(), result.url());

    }

}
