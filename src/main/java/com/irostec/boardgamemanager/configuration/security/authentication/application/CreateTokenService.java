package com.irostec.boardgamemanager.configuration.security.authentication.application;

import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.dependency.TokenFactory;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.CreateTokenError;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

/**
 * CreateTokenService
 * Creates a token that will be used to authenticate the user
 */
@AllArgsConstructor
public class CreateTokenService {

    private final TokenFactory tokenFactory;

    public Either<CreateTokenError, String> execute(String username, String password) {
        return Either.narrow(tokenFactory.buildNewToken(username, password));
    }

}
