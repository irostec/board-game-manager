package com.irostec.boardgamemanager.configuration.security.user.boundary.controller.output;

/**
 * AuthenticationResponse
 * Returned to a user that has been successfully authenticated
 */
public record AuthenticationResponse(String token) {}
