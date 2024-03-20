package com.irostec.boardgamemanager.configuration.security.user.boundary.controller;

import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.common.BGMBaseRestController;
import com.irostec.boardgamemanager.configuration.security.user.CreateUser;
import com.irostec.boardgamemanager.configuration.security.user.boundary.controller.input.UserCreationRequest;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedEmail;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedPassword;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUserCreationData;
import com.irostec.boardgamemanager.configuration.security.exception.UserCreationException;
import io.atlassian.fugue.Eithers;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.irostec.boardgamemanager.configuration.security.exception.UserValidationException;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import jakarta.validation.Valid;

/**
 * UserCreationController
 * Exposes the endpoints to create a user
 */
@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
class UserCreationController extends BGMBaseRestController {

    private static final String METHOD_NAME = "createUser";
    private final Logger logger = LogManager.getLogger(UserCreationController.class);

    private final CreateUser createUser;

    @PostMapping
    void createUser(@Valid @RequestBody UserCreationRequest userCreationRequest)
            throws UserValidationException, UserCreationException {

        final ValidatedUserCreationData validatedUserCreationData =
                new ValidatedUserCreationData(new ValidatedUsername(userCreationRequest.username()),
                        new ValidatedPassword(userCreationRequest.password()),
                        new ValidatedEmail(userCreationRequest.email()));

        Eithers.getOrThrow(
                this.createUser.execute(validatedUserCreationData)
                        .toEither()
                        .leftMap(exception -> new UserCreationException("Couldn't create user.", exception))
        );

    }

    @ExceptionHandler({UserCreationException.class})
    ResponseEntity<String> handle(UserCreationException ex) {

        LoggingUtils.error(logger, METHOD_NAME, ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

}
