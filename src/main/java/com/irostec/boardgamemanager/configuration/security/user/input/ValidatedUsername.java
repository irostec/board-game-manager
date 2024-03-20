package com.irostec.boardgamemanager.configuration.security.user.input;

import com.irostec.boardgamemanager.configuration.security.exception.UserValidationException;

/**
 * ValidatedUsername
 * A username that has been properly validated
 */
public final class ValidatedUsername {

    private static final String USERNAME_REGEX = "^[a-z0-9_-]{3,15}$";

    private final String value;

    public ValidatedUsername(String username) throws UserValidationException {

        if (!username.matches(USERNAME_REGEX)) {
            throw new UserValidationException(String.format("Invalid username: %s", username));
        }

        this.value = username;

    }

    public String value() {
        return this.value;
    }

}
