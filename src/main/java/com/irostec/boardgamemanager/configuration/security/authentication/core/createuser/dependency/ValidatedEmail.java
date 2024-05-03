package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * ValidatedEmail
 * An email that has been properly validated
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatedEmail {

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final String value;

    public static Validation<String, ValidatedEmail> of(String email) {

        return EMAIL_VALIDATOR.isValid(email) ?
                Validation.valid(new ValidatedEmail(email)) :
                Validation.invalid(String.format("Invalid email: %s", email));

    }

    public String value() {
        return this.value;
    }

}
