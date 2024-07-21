package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * ValidatedPassword
 * A password that has been properly validated
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatedPassword {

    private static final String ERROR_MESSAGE = """
            Invalid password. Please make sure that the password has:
            
            Minimum 8 characters in length.
            At least one uppercase English letter.
            At least one lowercase English letter.
            At least one digit.
            At least one special character.
            """;

    /*
        https://uibakery.io/regex-library/password-regex-java
        The regular expression below checks that a password has:

            Minimum 8 characters in length. Adjust it by modifying {8,}
            At least one uppercase English letter. You can remove this condition by removing (?=.*?[A-Z])
            At least one lowercase English letter.  You can remove this condition by removing (?=.*?[a-z])
            At least one digit. You can remove this condition by removing (?=.*?[0-9])
            At least one special character,  You can remove this condition by removing (?=.*?[#?!@$%^&*-])
    */
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    private final String value;

    public static Validation<String, ValidatedPassword> of(String password) {

        return password.matches(PASSWORD_REGEX) ?
                Validation.valid(new ValidatedPassword(password)) :
                Validation.invalid(ERROR_MESSAGE);

    }

    public String value() {
        return this.value;
    }

}
