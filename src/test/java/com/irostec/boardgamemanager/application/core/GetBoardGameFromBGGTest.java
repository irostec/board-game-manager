package com.irostec.boardgamemanager.application.core;

import com.irostec.boardgamemanager.TestingUtils;
import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.core.shared.BGGApi;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BoardGameNotFound;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.ExternalServiceFailure;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.InvalidBoardGameData;
import com.irostec.boardgamemanager.common.error.HttpError;
import com.irostec.boardgamemanager.common.error.NetworkFailure;
import com.irostec.boardgamemanager.common.error.UnsuccessfulResponse;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GetBoardGameFromBGGTest
 * Tests for the GetBoardGameFromBGG use case
 */
class GetBoardGameFromBGGTest {

    private static final String ID = "174430";

    private static final BGGApi BGG_API = externalId ->
            ID.equals(externalId) ?
                    Either.right(TestingUtils.buildBoardGameFromBGG()) :
                    Either.left(new BoardGameNotFound(externalId));

    @Test
    void givenAGetBoardGameFromBGGService_whenItIsExecutedWithAnUnknownId_thenTheResultShouldBeABoardGameNotFoundError() {

        final String unknownId = "someRandomId";

        final GetBoardGameFromBGG getBoardGameFromBGG = new GetBoardGameFromBGG(BGG_API);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGG.execute(unknownId);

        assertTrue(result.isLeft());
        assertTrue(
                switch (result.getLeft()) {
                    case BoardGameNotFound boardGameNotFound -> true;
                    case ExternalServiceFailure externalServiceFailure -> false;
                    case InvalidBoardGameData invalidBoardGameData -> false;
                }
        );

    }

    @ParameterizedTest
    @MethodSource("provideHttpErrors")
    void givenAGetBoardGameFromBGGService_whenItIsExecutedButTheExternalApiCallReturnsAnError_thenTheResultShouldBeAnExternalServiceFailure(HttpError httpError) {

        final BGGApi bggApi = externalId -> Either.left(new ExternalServiceFailure(externalId, httpError));
        final GetBoardGameFromBGG getBoardGameFromBGG = new GetBoardGameFromBGG(bggApi);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGG.execute(ID);

        assertTrue(result.isLeft());
        assertTrue(
                switch (result.getLeft()) {
                    case BoardGameNotFound boardGameNotFound -> false;
                    case ExternalServiceFailure externalServiceFailure -> true;
                    case InvalidBoardGameData invalidBoardGameData -> false;
                }
        );

    }

    @Test
    void givenAGetBoardGameFromBGGService_whenItIsSuccessfullyExecutedButTheResponseBodyIsInvalid_thenTheResultShouldBeAnInvalidBoardGameData() {

        final BGGApi bggApi = externalId -> Either.left(new InvalidBoardGameData(externalId, Collections.emptyMap()));
        final GetBoardGameFromBGG getBoardGameFromBGG = new GetBoardGameFromBGG(bggApi);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGG.execute(ID);

        assertTrue(result.isLeft());
        assertTrue(
                switch (result.getLeft()) {
                    case BoardGameNotFound boardGameNotFound -> false;
                    case ExternalServiceFailure externalServiceFailure -> false;
                    case InvalidBoardGameData invalidBoardGameData -> true;
                }
        );

    }

    @Test
    void givenAGetBoardGameFromBGGService_whenItIsExecutedWithAKnownId_thenTheResultShouldBeABoardGameFromBGGWithPartitionedLinks() {

        final GetBoardGameFromBGG getBoardGameFromBGG = new GetBoardGameFromBGG(BGG_API);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGG.execute(ID);

        assertTrue(result.isRight());
        assertEquals(ID, result.get().externalId());

    }

    private static Stream<HttpError> provideHttpErrors() {

        return Stream.of(
                new NetworkFailure(new RuntimeException()),
                new UnsuccessfulResponse(500, "Unknown error")
        );

    }

}
