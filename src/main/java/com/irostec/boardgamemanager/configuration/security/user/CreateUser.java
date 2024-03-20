package com.irostec.boardgamemanager.configuration.security.user;

import com.irostec.boardgamemanager.configuration.security.exception.UserCreationException;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUserCreationData;
import io.atlassian.fugue.Try;
import io.atlassian.fugue.Unit;

/**
 * CreateUser
 * Creates a user
 */
public interface CreateUser {

    Try<Unit> execute(ValidatedUserCreationData userData) throws UserCreationException;

}
