package com.irostec.boardgamemanager.configuration.security.exception;

import lombok.experimental.StandardException;

/**
 * InvalidTokenException
 * Thrown when the provided token is invalid
 */
@StandardException
public final class InvalidTokenException extends BGMSecurityException {}
