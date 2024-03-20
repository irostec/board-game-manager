package com.irostec.boardgamemanager.configuration.security.token.output;

import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;

/**
 * ValidatedToken
 * Represents a token that has been validated during the authentication process
 */
public record ValidatedToken(ValidatedUsername username) {}
