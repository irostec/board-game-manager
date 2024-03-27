package com.irostec.boardgamemanager.application.shared.bggapi.error;

import com.irostec.boardgamemanager.common.error.HttpError;

/**
 * ExternalServiceFailure
 * Represents that the proper communication with the BGGApi could not be established
 */
public record ExternalServiceFailure(String externalId, HttpError error) implements BGGApiError {}
