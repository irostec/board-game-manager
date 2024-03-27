package com.irostec.boardgamemanager.application.getboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.shared.bggapi.output.NameType;
import com.irostec.boardgamemanager.application.shared.bggapi.output.Name;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NameMapperTest
 */
class NameMapperTest {

    @ParameterizedTest
    @EnumSource(NameType.class)
    void roundtrip(NameType nameType) {

        final Name source = new Name(nameType, "Gloomhaven");

        final Name result = NameMapper.INSTANCE.map(NameMapper.INSTANCE.map(source));

        assertEquals(source, result);

    }

}
