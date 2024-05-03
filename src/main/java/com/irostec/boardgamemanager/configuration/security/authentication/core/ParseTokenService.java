package com.irostec.boardgamemanager.configuration.security.authentication.core;

import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error.ValidateTokenError;
import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.output.TokenData;
import io.vavr.control.Either;

/**
 * ParseTokenService
 * Extracts the relevant data from a token provided by the user
 */
public interface ParseTokenService {

    Either<ValidateTokenError, TokenData> execute(String jwtToken);

}
