package com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.error;

/**
 * InvalidFormat
 * Couldn't parse the user attributions retrieved from the database
 */
public record InvalidFormat(Throwable source) implements GetRolesAndPrivilegesError {}
