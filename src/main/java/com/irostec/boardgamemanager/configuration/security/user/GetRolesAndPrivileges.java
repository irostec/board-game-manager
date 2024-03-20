package com.irostec.boardgamemanager.configuration.security.user;

import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.exception.UserDatabaseException;
import com.irostec.boardgamemanager.configuration.security.user.output.RolesAndPrivileges;

import java.util.Optional;

/**
 * GetRolesAndPrivileges
 * Gets the roles and privileges granted to a user
 */
public interface GetRolesAndPrivileges {

    Optional<RolesAndPrivileges> execute(ValidatedUsername username) throws UserDatabaseException;

}
