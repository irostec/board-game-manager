package com.irostec.boardgamemanager.application;

import com.irostec.boardgamemanager.TestingUtils;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * FetchBoardGameFromBGGTest
 * Tests for the FetchBoardGameFromBGG use case
 */
class FetchBoardGameFromBGGTest {

    private static final String ID = "174430";

    private FetchBoardGameFromBGG fetchBoardGameFromBGG;

    @BeforeEach
    void init() throws BGMException {

        final BGGApi bggApi = externalId -> {
                if (ID.equals(externalId)) {
                    return TestingUtils.buildBoardGameFromBGG();
                } else {
                    throw new NotFoundException(String.format("No board game with id '%s' was found in boardgamegeek.com", externalId));
                }
        };

        fetchBoardGameFromBGG = new FetchBoardGameFromBGG(bggApi);

    }

    @Test
    void givenAFetchBoardGameFromBGGUseCase_whenItIsExecutedWithAnUnknownId_thenANotFoundExceptionShouldBeThrown() {

        final String unknownId = "someRandomId";

        assertThrows(NotFoundException.class, () -> fetchBoardGameFromBGG.execute(unknownId));

    }

    @Test
    void givenAFetchBoardGameFromBGGUseCase_whenItIsExecutedWithAKnownId_thenABoardGameFromBGGWithPartitionedLinksShouldBeReturned() throws BGMException {

        final BoardGameFromBGGWithPartitionedLinks result =
            fetchBoardGameFromBGG.execute(ID);

        assertEquals(ID, result.externalId());


    }

}
