package com.irostec.boardgamemanager.configuration.security.authentication.core;

import com.irostec.boardgamemanager.configuration.security.authentication.core.getuser.error.GetUserError;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getuser.output.BGMUser;
import io.vavr.control.Either;

import java.util.Optional;

/**
 * GetUser
 * Gets a user by username
 */
public interface GetUserService {

    Either<GetUserError, Optional<BGMUser>> execute(String username);

}
