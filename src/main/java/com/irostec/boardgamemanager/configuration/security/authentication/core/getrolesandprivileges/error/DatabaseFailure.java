package com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.error;

/**
 * DatabaseFailure
 * Couldn't read the user attributions from the database
 */
public record DatabaseFailure(Throwable cause) implements GetRolesAndPrivilegesError {}
