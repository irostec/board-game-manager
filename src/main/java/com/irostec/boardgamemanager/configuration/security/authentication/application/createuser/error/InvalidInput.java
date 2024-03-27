package com.irostec.boardgamemanager.configuration.security.authentication.application.createuser.error;

/**
 * InvalidInput
 * The data provided to create the user failed the validations
 */
public record InvalidInput(Iterable<String> errorMessages) implements CreateUserError {}
