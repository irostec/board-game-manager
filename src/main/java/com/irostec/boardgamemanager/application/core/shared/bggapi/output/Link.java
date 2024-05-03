package com.irostec.boardgamemanager.application.core.shared.bggapi.output;

/**
 * Link
 * A link to another thing, as represented in boardgamegeek.com
 */
public record Link(LinkType type, String id, String value) {}
