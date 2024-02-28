package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.shared.bggapi.output.NameType;
import com.irostec.boardgamemanager.common.exception.BGMException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NameMapperTest
 */
class NameMapperTest {

    @Test
    void mapName() throws BGMException {

        final String type = "primary";

        final com.irostec.boardgamemanager.application.shared.bggapi.output.Name source =
                new com.irostec.boardgamemanager.application.shared.bggapi.output.Name(
                        NameType.of(type),
                "Gloomhaven"
                );

        final com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.Name result =
                NameMapper.INSTANCE.mapName(source);

        assertEquals(type, result.type());
        assertEquals(source.value(), result.value());

    }

}
