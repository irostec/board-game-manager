package com.irostec.boardgamemanager.application.core.createuser.error;

public record PersistenceError(Throwable cause) implements CreateUserError {
}
