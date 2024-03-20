package com.irostec.boardgamemanager.configuration.security.user.boundary.controller.input;

import jakarta.validation.constraints.NotBlank;

/**
 * UserCreationRequest
 * Contains the data necessary to create a new user with the default configuration
 */
public record UserCreationRequest(@NotBlank(message = "username is mandatory") String username,
                                  @NotBlank(message = "password is mandatory") String password,
                                  @NotBlank(message = "email is mandatory") String email) {}
