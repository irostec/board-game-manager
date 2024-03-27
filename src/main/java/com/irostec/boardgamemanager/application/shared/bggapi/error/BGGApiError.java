package com.irostec.boardgamemanager.application.shared.bggapi.error;

/**
 * BGGApiError
 * Represents all the expected errors than can occur while using the BGGApi
 */
public sealed interface BGGApiError
        permits BoardGameNotFound, ExternalServiceFailure, InvalidBoardGameData {

    String externalId();

}
