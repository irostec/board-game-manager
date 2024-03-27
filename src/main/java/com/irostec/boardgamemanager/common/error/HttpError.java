package com.irostec.boardgamemanager.common.error;

/**
 * HttpError
 * Represents an error encountered while using an external HTTP API
 */
public sealed interface HttpError permits NetworkFailure, UnsuccessfulResponse {}
