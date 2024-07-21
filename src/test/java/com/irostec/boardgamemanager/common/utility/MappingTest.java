package com.irostec.boardgamemanager.common.utility;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.Test;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Name;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.NameType;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MappingUtilsTest
 * Validates that the transformation provided by MappingUtils behave correctly
 */
class MappingTest {

    @Test
    void toMap() {

        final NameType nameType = NameType.PRIMARY;
        final String value = "Gloomhaven";

        final Name name = new Name(nameType, value);

        final String keyForNameType = "nameType";
        final String keyForValue = "name";

        final Map<String, String> result = Mapping.toMap(
                name,
                ImmutablePair.of(n -> keyForNameType, n -> n.type().name()),
                ImmutablePair.of(n -> keyForValue, Name::value)
        );

        assertEquals(nameType.name(), result.get(keyForNameType));
        assertEquals(value, result.get(keyForValue));

    }

    @Test
    void toCollectionOfMaps() {

        final ImmutableTriple<Integer, String, String> representationsOfThree =
                ImmutableTriple.of(3, "three", "III");

        final List<ImmutableTriple<Integer, String, String>> representationsOfNumbers =
                List.of(representationsOfThree);

        final String keyForIntegerRepresentation = "asInteger";
        final String keyForEnglishName = "inEnglish";
        final String keyForRomanNumeral = "asRomanNumeral";

        final List<Map<String, String>> result = Mapping.toListOfMaps(
                representationsOfNumbers,
                ImmutablePair.of(representations -> keyForIntegerRepresentation, representations -> representations.getLeft().toString()),
                ImmutablePair.of(representations -> keyForEnglishName, ImmutableTriple::getMiddle),
                ImmutablePair.of(representations -> keyForRomanNumeral, ImmutableTriple::getRight)
        );

        final Map<String, String> mapOfRepresentationsOfThree = result.get(0);
        assertEquals(representationsOfThree.getLeft().toString(), mapOfRepresentationsOfThree.get(keyForIntegerRepresentation));
        assertEquals(representationsOfThree.getMiddle(), mapOfRepresentationsOfThree.get(keyForEnglishName));
        assertEquals(representationsOfThree.getRight(), mapOfRepresentationsOfThree.get(keyForRomanNumeral));

    }

}
