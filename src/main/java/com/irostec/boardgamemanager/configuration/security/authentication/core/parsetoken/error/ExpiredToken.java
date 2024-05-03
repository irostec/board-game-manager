package com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error;

/**
 * ExpiredToken
 * The provided token has expired
 */
public record ExpiredToken() implements ValidateTokenError {}
