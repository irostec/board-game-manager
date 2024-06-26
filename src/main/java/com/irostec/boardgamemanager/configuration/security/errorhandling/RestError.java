package com.irostec.boardgamemanager.configuration.security.errorhandling;

/**
 * RestError
 * A simple representation of a REST error detected during the authentication process
 */
public record RestError(String errorCode, String errorMessage) {}
