package com.irostec.boardgamemanager.application.shared.bggapi.output;

/**
 * Link
 * A link to another thing, as represented in boardgamegeek.com
 */
public record Link(LinkType type, String id, String value) {}
