package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createuser.caller;

import jakarta.validation.constraints.NotBlank;

/**
 * UserCreationRequest
 * Contains the data necessary to create a new user with the default configuration
 */
record UserCreationRequest(@NotBlank(message = "username is mandatory") String username,
                                  @NotBlank(message = "password is mandatory") String password,
                                  @NotBlank(message = "email is mandatory") String email) {}
