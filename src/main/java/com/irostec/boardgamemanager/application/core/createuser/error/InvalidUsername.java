package com.irostec.boardgamemanager.application.core.createuser.error;

public record InvalidUsername(String username) implements CreateUserError {
}