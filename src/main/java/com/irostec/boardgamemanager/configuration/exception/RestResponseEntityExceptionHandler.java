package com.irostec.boardgamemanager.configuration.exception;

import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * RestResponseEntityExceptionHandler
 * The global exception handler.
 * The last wall of defense for exceptions that were not managed anywhere else in the application.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LogManager.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler
    protected ResponseEntity<Object> handleUnexpectedException(
            Exception ex, WebRequest request) {

        LoggingUtils.error(logger, "handleUnexpectedException", ex);

        final String bodyOfResponse = "An unexpected error occurred. This will be notified to the system administrator.";

        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);

    }

}