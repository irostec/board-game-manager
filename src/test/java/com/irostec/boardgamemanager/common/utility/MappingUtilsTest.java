package com.irostec.boardgamemanager.common.utility;

import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MappingUtilsTest
 * Validates that the transformation provided by MappingUtils behave correctly
 */
class MappingUtilsTest {

    @Test
    void toMap() {

        final String name = "my_parameter";
        final boolean withDecryption = true;

        final GetParameterRequest getParameterRequest = new GetParameterRequest()
                .withName(name)
                .withWithDecryption(withDecryption);

        final String keyForName = "name";
        final String keyForWithDecryption = "withDecryption";

        final Map<String, String> result = MappingUtils.toMap(
                getParameterRequest,
                ImmutablePair.of(request -> keyForName, GetParameterRequest::getName),
                ImmutablePair.of(request -> keyForWithDecryption, request -> String.valueOf(request.getWithDecryption()))
        );

        assertEquals(name, result.get(keyForName));
        assertEquals(String.valueOf(withDecryption), result.get(keyForWithDecryption));

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

        final List<Map<String, String>> result = MappingUtils.toListOfMaps(
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
