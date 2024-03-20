package com.irostec.boardgamemanager.configuration.security.token;

import com.irostec.boardgamemanager.configuration.security.token.output.ValidatedToken;
import com.irostec.boardgamemanager.configuration.security.exception.InvalidTokenException;
import io.atlassian.fugue.Either;

/**
 * Validates a token provided by the user
 */
public interface ValidateToken {

    Either<InvalidTokenException, ValidatedToken> execute(String jwtToken);

}
