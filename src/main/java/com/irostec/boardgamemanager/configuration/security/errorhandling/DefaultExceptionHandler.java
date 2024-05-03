package com.irostec.boardgamemanager.configuration.security.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * DefaultExceptionHandler
 * Exception handler specifically tailored for AuthenticationException
 */
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<RestError> handleAuthenticationException(AuthenticationException ex) {

        final RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(),
                "Authentication failed at controller advice");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);

    }

}
