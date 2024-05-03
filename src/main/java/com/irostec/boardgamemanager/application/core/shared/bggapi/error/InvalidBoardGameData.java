package com.irostec.boardgamemanager.application.core.shared.bggapi.error;

import java.util.Map;

/**
 * InvalidBoardGameData
 * Represents that the BGGApi returned the requested board game, but it had data that was unacceptable according to the validation rules
 */
public record InvalidBoardGameData(String externalId, Map<String, Iterable<String>> errorMessagesByPropertyName)
        implements BGGApiError {}