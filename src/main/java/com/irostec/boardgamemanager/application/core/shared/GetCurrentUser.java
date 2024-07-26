package com.irostec.boardgamemanager.application.core.shared;

import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.GetCurrentUserError;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.output.User;
import io.vavr.control.Either;

public interface GetCurrentUser {

    Either<GetCurrentUserError, User> execute();

}
