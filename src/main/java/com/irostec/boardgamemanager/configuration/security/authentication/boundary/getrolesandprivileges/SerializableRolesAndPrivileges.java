package com.irostec.boardgamemanager.configuration.security.authentication.boundary.getrolesandprivileges;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.irostec.boardgamemanager.configuration.security.authentication.application.getrolesandprivileges.output.BGMRole;
import com.irostec.boardgamemanager.configuration.security.authentication.application.getrolesandprivileges.output.RolesAndPrivileges;

import java.util.Set;

import static com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUserTable.Attributes.ROLE_SET;

/**
 * SerializableRolesAndPrivileges
 * A RolesAndPrivileges that can be converted to and from JSON using the Jackson library
 */
record SerializableRolesAndPrivileges(@JsonAlias(ROLE_SET) Set<BGMRole> roles) implements RolesAndPrivileges {}
