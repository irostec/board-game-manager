package com.irostec.boardgamemanager.configuration.security.authentication.application.getuser.error;

/**
 * GetUserError
 * Represents the different anomalous situations that can arise during the GetUser use case
 */
public record GetUserError(Throwable cause) {}
