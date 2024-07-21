package com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error;

public sealed interface GetCurrentUserError permits UserNotAuthenticated, UserNotFound, DatabaseError {
}
