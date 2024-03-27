package com.irostec.boardgamemanager.application.shared.bggapi.error;

/**
 * BoardGameNotFound
 * Represents that a board game with the specified external id was not found in the BGGApi
 */
public record BoardGameNotFound(String externalId) implements BGGApiError {}
