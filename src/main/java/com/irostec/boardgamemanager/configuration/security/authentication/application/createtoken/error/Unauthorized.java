package com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error;

public record Unauthorized(Throwable cause) implements CreateTokenError {}
