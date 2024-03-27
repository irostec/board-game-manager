package com.irostec.boardgamemanager.configuration.security.authentication.application;

import com.irostec.boardgamemanager.configuration.security.authentication.application.getrolesandprivileges.error.GetRolesAndPrivilegesError;
import com.irostec.boardgamemanager.configuration.security.authentication.application.getrolesandprivileges.output.RolesAndPrivileges;
import io.vavr.control.Either;

import java.util.Optional;

/**
 * GetRolesAndPrivilegesService
 * Gets the roles and privileges assigned to a user
 */
public interface GetRolesAndPrivilegesService {

    Either<GetRolesAndPrivilegesError, Optional<RolesAndPrivileges>> execute(String username);

}
