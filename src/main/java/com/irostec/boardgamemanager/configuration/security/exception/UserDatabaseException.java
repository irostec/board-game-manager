package com.irostec.boardgamemanager.configuration.security.exception;

import lombok.experimental.StandardException;

/**
 * UserDatabaseException
 * Thrown when an error is encountered while communicating with the user database
 */
@StandardException
public final class UserDatabaseException extends BGMSecurityException {}
