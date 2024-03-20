package com.irostec.boardgamemanager.configuration.security.user.input;

/**
 * ValidatedUserCreationData
 * A container for user-provided data that has been properly validated
 */
public record ValidatedUserCreationData(ValidatedUsername username,
                                        ValidatedPassword password,
                                        ValidatedEmail email) {}
