package com.irostec.boardgamemanager.configuration.security.user.input;

import com.irostec.boardgamemanager.configuration.security.exception.UserValidationException;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * ValidatedEmail
 * An email that has been properly validated
 */
public final class ValidatedEmail {

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final String value;

    public ValidatedEmail(String email) throws UserValidationException {

        if (!EMAIL_VALIDATOR.isValid(email)) {
            throw new UserValidationException(String.format("Invalid email: ", email));
        }

        this.value = email;

    }

    public String value() {
        return this.value;
    }

}
