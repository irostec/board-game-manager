package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.shared.bggapi.output.ImageType;
import com.irostec.boardgamemanager.application.shared.bggapi.output.Image;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ImageMapperTest
 */
class ImageMapperTest {

    @ParameterizedTest
    @EnumSource(ImageType.class)
    void roundtrip(ImageType imageType) {

        final Image source =
                new Image(
                        imageType,
                        "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__original/img/7d-lj5Gd1e8PFnD97LYFah2c45M=/0x0/filters:format(jpeg)/pic2437871.jpg"
                );

        final Image result = ImageMapper.INSTANCE.map(ImageMapper.INSTANCE.map(source));

        assertEquals(source, result);

    }

}
