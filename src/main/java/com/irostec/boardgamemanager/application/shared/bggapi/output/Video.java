package com.irostec.boardgamemanager.application.shared.bggapi.output;

/**
 * Video
 * A video, as represented in boardgamegeek.com
 */
public record Video(String id, String title, String category, String language, String link) {}
