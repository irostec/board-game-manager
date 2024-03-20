package com.irostec.boardgamemanager.configuration.security.exception;

import lombok.experimental.StandardException;

/**
 * UserValidationException
 * Thrown when the user validation fails (e.g., the provided password doesn't fulfill the strength requirements)
 */
@StandardException
public final class UserValidationException  extends BGMSecurityException {}
