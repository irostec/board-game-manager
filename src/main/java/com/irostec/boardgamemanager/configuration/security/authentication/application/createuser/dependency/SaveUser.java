package com.irostec.boardgamemanager.configuration.security.authentication.application.createuser.dependency;

import com.irostec.boardgamemanager.configuration.security.authentication.application.createuser.error.PersistenceFailure;
import io.vavr.control.Either;

/**
 * SaveUser
 * Persists a user into some kind of storage
 */
public interface SaveUser {

    Either<PersistenceFailure, Void> execute(ValidatedUserCreationData userData);

}
