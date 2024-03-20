package com.irostec.boardgamemanager.configuration.security.exception;

import lombok.experimental.StandardException;

/**
 * UserAuthenticationException
 * Thrown when the user authentication process fails (e.g., because invalid credentials were provided)
 */
@StandardException
public final class UserAuthenticationException extends BGMSecurityException {}
