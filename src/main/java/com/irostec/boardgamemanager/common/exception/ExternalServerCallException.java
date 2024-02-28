package com.irostec.boardgamemanager.common.exception;

import lombok.experimental.StandardException;

/**
 * ExternalServerCallException
 * Thrown when a network error occurs while trying to communicate with an external server
 */
@StandardException
public final class ExternalServerCallException extends BGMException {}
