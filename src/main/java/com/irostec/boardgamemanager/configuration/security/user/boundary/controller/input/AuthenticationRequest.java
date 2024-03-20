package com.irostec.boardgamemanager.configuration.security.user.boundary.controller.input;

import jakarta.validation.constraints.NotBlank;

/**
 * AuthenticationRequest
 * Contains the data provided by a user who wishes to log in
 */
public record AuthenticationRequest(@NotBlank(message = "username is mandatory") String username,
                                    @NotBlank(message = "password is mandatory") String password) {}
