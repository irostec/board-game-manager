package com.irostec.boardgamemanager.configuration.security.authentication.core;

import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.error.GetRolesAndPrivilegesError;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.RolesAndPrivileges;
import io.vavr.control.Either;

import java.util.Optional;

/**
 * GetRolesAndPrivilegesService
 * Gets the roles and privileges assigned to a user
 */
public interface GetRolesAndPrivilegesService {

    Either<GetRolesAndPrivilegesError, Optional<RolesAndPrivileges>> execute(String username);

}
