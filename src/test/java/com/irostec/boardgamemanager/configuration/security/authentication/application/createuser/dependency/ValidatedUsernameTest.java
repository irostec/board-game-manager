package com.irostec.boardgamemanager.configuration.security.authentication.application.createuser.dependency;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ValidatedUsernameTest
 * Verifies that the validations performed during the initialization of ValidatedUsername instances are working correctly
 */
class ValidatedUsernameTest {

    @Test
    void givenAValidatedUsername_whenItIsInitializedWithAnInvalidInput_thenTheValidationShouldFail() {

        final String invalidUsername = "¡nval¡d";

        final Validation<String, ValidatedUsername> result = ValidatedUsername.of(invalidUsername);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAValidatedUsername_whenItIsInitializedWithAValidInput_thenTheValidationShouldSucceed() {

        final String validUsername = "irostec";

        final Validation<String, ValidatedUsername> result = ValidatedUsername.of(validUsername);

        assertTrue(result.isValid());
        assertEquals(validUsername, result.get().value());

    }

}
