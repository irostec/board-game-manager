package com.irostec.boardgamemanager.common.error;

/**
 * UnsuccessfulResponse
 * Means that the HTTP communication was executed, but the response received from the external API indicated a problem
 */
public record UnsuccessfulResponse(int httpCode, String message) implements HttpError {}
