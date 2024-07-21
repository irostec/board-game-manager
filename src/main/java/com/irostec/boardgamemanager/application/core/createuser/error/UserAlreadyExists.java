package com.irostec.boardgamemanager.application.core.createuser.error;

public record UserAlreadyExists(long id, String username) implements CreateUserError {
}
