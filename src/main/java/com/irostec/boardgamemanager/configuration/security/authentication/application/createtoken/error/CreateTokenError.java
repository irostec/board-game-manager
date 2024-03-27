package com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error;

/**
 * CreateTokenError
 * Represent the different ways that the creation of an authentication token can fail
 */
public sealed interface CreateTokenError permits Unauthorized {}
