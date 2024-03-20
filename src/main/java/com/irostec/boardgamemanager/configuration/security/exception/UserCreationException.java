package com.irostec.boardgamemanager.configuration.security.exception;

import lombok.experimental.StandardException;

/**
 * UserCreationException
 * Thrown when an attempt to create a new user fails due to constraint violations (e.g., the email is already in use)
 */
@StandardException
public final class UserCreationException extends BGMSecurityException {}
