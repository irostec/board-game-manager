package com.irostec.boardgamemanager.common.error;

/**
 * NetworkFailure
 * Means that the HTTP communication failed due to an error in the network
 */
public record NetworkFailure(Throwable source) implements HttpError {}
