package com.irostec.boardgamemanager.configuration.security.authentication.application.getrolesandprivileges.error;

/**
 * GetRolesAndPrivilegesError
 * Represents the different anomalous situations that can arise during the GetRolesAndPrivileges use case
 */
public sealed interface GetRolesAndPrivilegesError permits DatabaseFailure, InvalidFormat {}
