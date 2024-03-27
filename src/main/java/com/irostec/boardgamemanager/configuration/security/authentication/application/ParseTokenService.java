package com.irostec.boardgamemanager.configuration.security.authentication.application;

import com.irostec.boardgamemanager.configuration.security.authentication.application.parsetoken.error.ValidateTokenError;
import com.irostec.boardgamemanager.configuration.security.authentication.application.parsetoken.output.TokenData;
import io.vavr.control.Either;

/**
 * ParseTokenService
 * Extracts the relevant data from a token provided by the user
 */
public interface ParseTokenService {

    Either<ValidateTokenError, TokenData> execute(String jwtToken);

}
