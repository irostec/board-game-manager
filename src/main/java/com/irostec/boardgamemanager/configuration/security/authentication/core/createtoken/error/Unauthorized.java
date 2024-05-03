package com.irostec.boardgamemanager.configuration.security.authentication.core.createtoken.error;

public record Unauthorized(Throwable cause) implements CreateTokenError {}
