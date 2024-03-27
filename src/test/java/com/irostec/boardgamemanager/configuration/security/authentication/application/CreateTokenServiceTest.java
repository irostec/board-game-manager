package com.irostec.boardgamemanager.configuration.security.authentication.application;

import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.dependency.TokenFactory;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.CreateTokenError;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.Unauthorized;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CreateTokenServiceTest
 * Verifies that the CreateToken use case behaves as expected
 */
class CreateTokenServiceTest {

    private static final String USERNAME = "irostec";
    private static final String PASSWORD = "V3ry$3cur3";
    private static final String JWT_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9";

    private static final TokenFactory SUCCESSFUL_TOKEN_FACTORY = (username, password) -> Either.right(JWT_TOKEN);
    private static final TokenFactory UNSUCCESSFUL_TOKEN_FACTORY = (username, password) -> Either.left(new Unauthorized(new RuntimeException()));

    @Test
    void givenACreateTokenService_whenItIsExecutedAndTheAuthorizationIsUnsuccessful_thenItShouldFailWithAnUnauthorizedError() {

        final CreateTokenService createTokenService = new CreateTokenService(UNSUCCESSFUL_TOKEN_FACTORY);

        final Either<CreateTokenError, String> result = createTokenService.execute(USERNAME, PASSWORD);

        assertTrue(result.isLeft());
        assertTrue(
                switch (result.getLeft()) {
                    case Unauthorized unauthorized -> true;
                }
        );

    }

    @Test
    void givenACreateTokenService_whenItIsExecutedAndTheAuthorizationIsSuccessful_thenItShouldSucceed() {

        final CreateTokenService createTokenService = new CreateTokenService(SUCCESSFUL_TOKEN_FACTORY);

        final Either<CreateTokenError, String> result = createTokenService.execute(USERNAME, PASSWORD);

        assertTrue(result.isRight());
        assertEquals(JWT_TOKEN, result.get());

    }


}