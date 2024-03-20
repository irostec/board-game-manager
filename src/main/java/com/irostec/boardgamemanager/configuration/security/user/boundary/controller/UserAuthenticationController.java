package com.irostec.boardgamemanager.configuration.security.user.boundary.controller;

import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.common.BGMBaseRestController;
import com.irostec.boardgamemanager.configuration.security.token.CreateToken;
import com.irostec.boardgamemanager.configuration.security.user.boundary.controller.input.AuthenticationRequest;
import com.irostec.boardgamemanager.configuration.security.user.boundary.controller.output.AuthenticationResponse;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedPassword;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.exception.UserAuthenticationException;
import com.irostec.boardgamemanager.configuration.security.exception.UserValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserAuthenticationController
 * Exposes the endpoints to authenticate a user
 */
@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
class UserAuthenticationController extends BGMBaseRestController {

    private static final String METHOD_NAME = "authenticateUser";
    private final Logger logger = LogManager.getLogger(UserAuthenticationController.class);

    private final CreateToken createToken;

    @PostMapping("/authenticate")
    AuthenticationResponse authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest)
            throws UserValidationException, UserAuthenticationException {

        final ValidatedUsername username = new ValidatedUsername(authenticationRequest.username());
        final ValidatedPassword password = new ValidatedPassword(authenticationRequest.password());

        final String token = this.createToken.execute(username, password);

        return new AuthenticationResponse(token);

    }

    @ExceptionHandler({UserValidationException.class})
    ResponseEntity<String> handle(UserValidationException ex) {

        LoggingUtils.error(logger, METHOD_NAME, ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({UserAuthenticationException.class})
    ResponseEntity<String> handle(UserAuthenticationException ex) {

        LoggingUtils.error(logger, METHOD_NAME, ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);

    }

}
