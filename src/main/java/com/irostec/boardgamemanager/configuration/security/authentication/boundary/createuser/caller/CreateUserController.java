package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createuser.caller;

import com.irostec.boardgamemanager.common.utility.Logging;
import com.irostec.boardgamemanager.configuration.security.authentication.core.CreateUserService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.CreateUserError;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.InvalidInput;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.PersistenceFailure;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.input.UserData;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * CreateUserController
 * Exposes the endpoint to create a user
 */
@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
class CreateUserController {

    private static final String METHOD_NAME = "createUser";
    private final Logger logger = LogManager.getLogger(CreateUserController.class);

    private final CreateUserService createUserService;

    @PostMapping
    ResponseEntity<?> createUser(@Valid @RequestBody UserCreationRequest userCreationRequest) {

        final UserData userData = new UserData(
                userCreationRequest.username(),
                userCreationRequest.password(),
                userCreationRequest.email()
        );

        return createUserService.execute(userData)
                .peekLeft(this::logError)
                .fold(CreateUserController::errorToHttpResponse, ResponseEntity::ok);

    }

    private void logError(CreateUserError error) {

        switch (error) {
            case InvalidInput invalidInput:
                ;
                break;
            case PersistenceFailure persistenceFailure:
                final String persistenceFailureMessage = "Couldn't create the user.";
                Logging.error(logger, METHOD_NAME, persistenceFailureMessage, persistenceFailure.exception());
                break;
        }

    }

    private static ResponseEntity<String> errorToHttpResponse(CreateUserError error) {

        return switch (error) {
            case InvalidInput invalidInput -> {
                final String invalidInputMessage =
                        String.format(
                                "The data provided to create the new user is invalid, due to the following reasons:\n\n%s",
                                StreamSupport.stream(invalidInput.errorMessages().spliterator(), false)
                                        .collect(Collectors.joining("\n"))
                        );
                yield new ResponseEntity<>(invalidInputMessage, HttpStatus.BAD_REQUEST);
            }
            case PersistenceFailure persistenceFailure -> {
                final String persistenceFailureMessage = "The user couldn't be persisted. Some of the possible reasons may be: a) The email already exists; b) The database is overloaded. Please try again, and if the problem persists, contact an administrator.";
                yield new ResponseEntity<>(persistenceFailureMessage, HttpStatus.CONFLICT);
            }
        };

    }

}
