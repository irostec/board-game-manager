package com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error;

/**
 * InvalidToken
 * The provided token doesn't conform to the expected format
 */
public record InvalidToken(Throwable cause) implements ValidateTokenError {}
