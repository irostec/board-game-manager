package com.irostec.boardgamemanager.application.shared.bggapi.output;

/**
 * Name
 * A name, as represented in boardgamegeek.com
 */
public record Name(NameType type, String value) {}
