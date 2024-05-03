package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency;

import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ValidatedEmailTest
 * Verifies that the validations performed during the initialization of ValidatedEmail instances are working correctly
 */
class ValidatedEmailTest {

    @Test
    void givenAValidatedEmail_whenItIsInitializedWithAnInvalidInput_thenTheValidationShouldFail() {

        final String invalidEmail = "irostec @gmail.com";

        final Validation<String, ValidatedEmail> result = ValidatedEmail.of(invalidEmail);

        assertTrue(result.isInvalid());

    }

    @Test
    void givenAValidatedEmail_whenItIsInitializedWithAValidInput_thenTheValidationShouldSucceed() {

        final String validEmail = "irostec@gmail.com";

        final Validation<String, ValidatedEmail> result = ValidatedEmail.of(validEmail);

        assertTrue(result.isValid());
        assertEquals(validEmail, result.get().value());

    }

}
