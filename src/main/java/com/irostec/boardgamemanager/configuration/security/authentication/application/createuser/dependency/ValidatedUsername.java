package com.irostec.boardgamemanager.configuration.security.authentication.application.createuser.dependency;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * ValidatedUsername
 * A username that has been properly validated
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatedUsername {

    private static final String USERNAME_REGEX = "^[a-z0-9_-]{3,15}$";

    private final String value;

    public static Validation<String, ValidatedUsername> of(String username) {

        return username.matches(USERNAME_REGEX) ?
                Validation.valid(new ValidatedUsername(username)) :
                Validation.invalid(String.format("Invalid username: %s", username));

    }

    public String value() {
        return this.value;
    }

}
