package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createtoken.caller;

import jakarta.validation.constraints.NotBlank;

/**
 * AuthenticationRequest
 * Contains the data provided by a user who wishes to log in
 */
record AuthenticationRequest(@NotBlank(message = "username is mandatory") String username,
                                    @NotBlank(message = "password is mandatory") String password) {}
