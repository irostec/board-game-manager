package com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.input;

/**
 * UserData
 * An unvalidated representation of the data required to create a user
 */
public record UserData(String username, String password, String email) {}
