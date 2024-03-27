package com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.dependency;

import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.Unauthorized;
import io.vavr.control.Either;

/**
 * TokenFactory
 * Creates authentication tokens for a given user (identified by credentials)
 */
public interface TokenFactory {

    Either<Unauthorized, String> buildNewToken(String username, String password);

}
