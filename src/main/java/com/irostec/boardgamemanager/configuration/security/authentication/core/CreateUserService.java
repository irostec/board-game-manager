package com.irostec.boardgamemanager.configuration.security.authentication.core;

import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.SaveUser;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.ValidatedEmail;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.ValidatedPassword;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.ValidatedUserCreationData;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.input.UserData;
import io.vavr.control.Either;
import io.vavr.control.Validation;

import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.InvalidInput;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.CreateUserError;
import lombok.AllArgsConstructor;

/**
 * CreateUserService
 * Creates a user
 */
@AllArgsConstructor
public class CreateUserService {

    private final SaveUser saveUser;

    public Either<CreateUserError, Void> execute(UserData userData) {

        final Validation<String, ValidatedUsername> usernameValidation = ValidatedUsername.of(userData.username());
        final Validation<String, ValidatedPassword> passwordValidation = ValidatedPassword.of(userData.password());
        final Validation<String, ValidatedEmail> emailValidation = ValidatedEmail.of(userData.email());

        final Validation<CreateUserError, ValidatedUserCreationData> userCreationDataValidation =
                Validation.combine(usernameValidation, passwordValidation, emailValidation)
                .ap(ValidatedUserCreationData::new)
                .mapError(InvalidInput::new);

        return userCreationDataValidation.map(saveUser::execute)
                .toEither()
                .flatMap(Either::narrow);

    }

}
