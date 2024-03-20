package com.irostec.boardgamemanager.configuration.security.user;

import com.irostec.boardgamemanager.configuration.security.exception.UserDatabaseException;
import com.irostec.boardgamemanager.configuration.security.user.output.BGMUser;

import java.util.Optional;

/**
 * GetUser
 * Gets a user by username
 */
public interface GetUser {

    Optional<? extends BGMUser> execute(String username) throws UserDatabaseException;

}
