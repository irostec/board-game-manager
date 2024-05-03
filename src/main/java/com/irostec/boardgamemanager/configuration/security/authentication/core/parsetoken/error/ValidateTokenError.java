package com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error;

/**
 * ValidateTokenError
 * Represents the different anomalous situations that can arise during the ValidateToken use case
 */
public sealed interface ValidateTokenError permits InvalidToken, ExpiredToken {}
