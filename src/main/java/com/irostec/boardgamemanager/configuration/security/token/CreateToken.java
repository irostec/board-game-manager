package com.irostec.boardgamemanager.configuration.security.token;

import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedPassword;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.exception.UserAuthenticationException;

/**
 * CreateToken
 * Creates a token that will be used to authenticate the user
 */
public interface CreateToken {

    String execute(ValidatedUsername username, ValidatedPassword password) throws UserAuthenticationException;

}
