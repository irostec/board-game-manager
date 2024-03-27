package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createtoken.caller;

import com.irostec.boardgamemanager.configuration.security.authentication.application.CreateTokenService;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.CreateTokenError;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.Unauthorized;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CreateTokenController
 * Provides a token to the user after authentication
 */
@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
class CreateTokenController {

    private final CreateTokenService createTokenService;

    @PostMapping("/authenticate")
    ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        return this.createTokenService.execute(authenticationRequest.username(), authenticationRequest.password())
                .fold(
                        CreateTokenController::errorToHttpResponse,
                        token -> new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK)
                );

    }

    private static ResponseEntity<String> errorToHttpResponse(CreateTokenError error) {

        return switch (error) {
            case Unauthorized unauthorized ->
                    new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);

        };

    }

}
