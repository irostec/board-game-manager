package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

/**
 * Name
 * A name, as represented in boardgamegeek.com
 */
public record Name(NameType type, String value) {}
