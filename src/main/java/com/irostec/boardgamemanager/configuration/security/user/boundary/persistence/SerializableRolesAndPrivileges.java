package com.irostec.boardgamemanager.configuration.security.user.boundary.persistence;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.irostec.boardgamemanager.configuration.security.user.output.BGMRole;
import com.irostec.boardgamemanager.configuration.security.user.output.RolesAndPrivileges;

import java.util.Set;

import static com.irostec.boardgamemanager.configuration.security.user.boundary.persistence.DynamoDbUserTable.Attributes.ROLE_SET;

/**
 * SerializableRolesAndPrivileges
 * A RolesAndPrivileges that can be converted to and from JSON using the Jackson library
 */
record SerializableRolesAndPrivileges(@JsonAlias(ROLE_SET) Set<BGMRole> roles) implements RolesAndPrivileges {}
