package com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error;

public record UserNotFound(String username) implements GetCurrentUserError {
}
