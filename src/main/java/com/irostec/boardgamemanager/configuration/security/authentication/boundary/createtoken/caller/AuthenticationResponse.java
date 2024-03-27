package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createtoken.caller;

/**
 * AuthenticationResponse
 * Returned to a user that has been successfully authenticated
 */
record AuthenticationResponse(String token) {}
