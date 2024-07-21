package com.irostec.boardgamemanager.application.core.createuser.error;

public sealed interface CreateUserError permits UserAlreadyExists, InvalidUsername, PersistenceError {
}
