package com.irostec.boardgamemanager.configuration.security.authentication.application.createuser.dependency;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ValidatedPasswordTest
 * Verifies that the validations performed during the initialization of ValidatedPassword instances are working correctly
 */
class ValidatedPasswordTest {

    @Test
    void givenAValidatedPassword_whenItIsInitializedWithAnInvalidInput_thenTheValidationShouldFail() {

        final String invalidPassword = "S#0rt";

        final Validation<String, ValidatedPassword> result = ValidatedPassword.of(invalidPassword);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAValidatedPassword_whenItIsInitializedWithAValidInput_thenTheValidationShouldSucceed() {

        final String validPassword = "V3ry$3cur3";

        final Validation<String, ValidatedPassword> result = ValidatedPassword.of(validPassword);

        assertTrue(result.isValid());
        assertEquals(validPassword, result.get().value());

    }

}
