package com.irostec.boardgamemanager.common.exception;

import lombok.experimental.StandardException;

/**
 * ExternalServerHttpException
 * Thrown when an HTTP error code is returned by an external server
 */
@StandardException
public final class ExternalServerHttpException extends BGMException {}
