package com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error;

public record DatabaseError(Throwable cause) implements GetCurrentUserError {
}
