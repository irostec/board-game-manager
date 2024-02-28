package com.irostec.boardgamemanager.common.exception;

import lombok.experimental.StandardException;

/**
 * NotFoundException
 * Thrown when a single item is expected as the result of an operation, but it is not found
 */
@StandardException
public final class NotFoundException extends BGMException {}
