package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error;

/**
 * CreateUserError
 * Represents the different anomalous situations that can arise during the CreateUser use case
 */
public sealed interface CreateUserError permits InvalidInput, PersistenceFailure {}
