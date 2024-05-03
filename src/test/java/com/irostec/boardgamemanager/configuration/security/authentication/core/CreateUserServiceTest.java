package com.irostec.boardgamemanager.configuration.security.authentication.core;

import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.SaveUser;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.CreateUserError;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.InvalidInput;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.PersistenceFailure;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.input.UserData;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static com.google.common.base.Defaults.defaultValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CreateUserServiceTest
 * Verifies that the CreateUser use case behaves as expected
 */
class CreateUserServiceTest {

    private static final String VALID_USERNAME = "irostec";
    private static final String VALID_PASSWORD = "V3ry$3cur3";
    private static final String VALID_EMAIL = "irostec@gmail.com";

    private static final SaveUser SAVE_USER = userData -> Either.right(defaultValue(Void.class));

    @Test
    void givenACreateUserService_whenItIsExecutedWithInvalidInput_thenItShouldFailWithAnInvalidInputError() {

        final CreateUserService createUserService = new CreateUserService(SAVE_USER);

        final String invalidPassword = "S#0rt";
        final UserData userData = new UserData(VALID_USERNAME, invalidPassword,VALID_EMAIL);

        final Either<CreateUserError, Void> result = createUserService.execute(userData);

        assertTrue(result.isLeft());
        assertTrue(
                switch (result.getLeft()) {
                    case InvalidInput invalidInput -> true;
                    case PersistenceFailure persistenceFailure -> false;
                }
        );

    }

    @Test
    void givenACreateUserService_whenItIsExecutedWithValidInputButTheTransactionCannotBeCompleted_thenItShouldFailWithAPersistenceFailure() {

        final SaveUser saveUser = userData -> Either.left(new PersistenceFailure(new RuntimeException()));
        final CreateUserService createUserService = new CreateUserService(saveUser);

        final UserData userData = new UserData(VALID_USERNAME, VALID_PASSWORD,VALID_EMAIL);

        final Either<CreateUserError, Void> result = createUserService.execute(userData);

        assertTrue(result.isLeft());
        assertTrue(
                switch (result.getLeft()) {
                    case InvalidInput invalidInput -> false;
                    case PersistenceFailure persistenceFailure -> true;
                }
        );

    }

    @Test
    void givenACreateUserService_whenItIsExecutedWithValidInputAndTheTransactionIsSuccessfullyCompleted_thenItShouldSucceed() {

        final CreateUserService createUserService = new CreateUserService(SAVE_USER);
        final UserData userData = new UserData(VALID_USERNAME, VALID_PASSWORD,VALID_EMAIL);

        final Either<CreateUserError, Void> result = createUserService.execute(userData);

        assertTrue(result.isRight());

    }

}
