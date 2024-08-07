package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency;

import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.ValidatedUserCreationData;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.PersistenceFailure;
import io.vavr.control.Either;

/**
 * SaveUser
 * Persists a user into some kind of storage
 */
public interface SaveUser {

    Either<PersistenceFailure, Void> execute(ValidatedUserCreationData userData);

}
