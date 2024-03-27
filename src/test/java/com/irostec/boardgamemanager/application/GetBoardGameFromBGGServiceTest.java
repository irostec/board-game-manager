package com.irostec.boardgamemanager.application;

import com.irostec.boardgamemanager.TestingUtils;
import com.irostec.boardgamemanager.application.getboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import com.irostec.boardgamemanager.application.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.shared.bggapi.error.BoardGameNotFound;
import com.irostec.boardgamemanager.application.shared.bggapi.error.ExternalServiceFailure;
import com.irostec.boardgamemanager.application.shared.bggapi.error.InvalidBoardGameData;
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
class GetBoardGameFromBGGServiceTest {

    private static final String ID = "174430";

    private static final BGGApi BGG_API = externalId ->
            ID.equals(externalId) ?
                    Either.right(TestingUtils.buildBoardGameFromBGG()) :
                    Either.left(new BoardGameNotFound(externalId));

    @Test
    void givenAGetBoardGameFromBGGService_whenItIsExecutedWithAnUnknownId_thenTheResultShouldBeABoardGameNotFoundError() {

        final String unknownId = "someRandomId";

        final GetBoardGameFromBGGService getBoardGameFromBGGService = new GetBoardGameFromBGGService(BGG_API);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGGService.execute(unknownId);

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
        final GetBoardGameFromBGGService getBoardGameFromBGGService = new GetBoardGameFromBGGService(bggApi);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGGService.execute(ID);

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
        final GetBoardGameFromBGGService getBoardGameFromBGGService = new GetBoardGameFromBGGService(bggApi);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGGService.execute(ID);

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

        final GetBoardGameFromBGGService getBoardGameFromBGGService = new GetBoardGameFromBGGService(BGG_API);

        final Either<BGGApiError, BoardGameFromBGGWithPartitionedLinks> result = getBoardGameFromBGGService.execute(ID);

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
