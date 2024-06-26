package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error;

/**
 * PersistenceFailure
 * The database could not commit the transaction
 */
public record PersistenceFailure(Throwable exception) implements CreateUserError {}
