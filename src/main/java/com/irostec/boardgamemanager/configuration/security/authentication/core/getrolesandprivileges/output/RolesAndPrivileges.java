package com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output;

import java.util.Set;

/**
 * RolesAndPrivileges
 * Contains the roles and privileges granted to a user
 */
public interface RolesAndPrivileges {

    Set<BGMRole> roles();

}
