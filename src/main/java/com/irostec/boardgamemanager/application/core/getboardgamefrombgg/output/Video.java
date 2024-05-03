package com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output;

/**
 * Video
 * A video about a board game, as represented in the GetBoardGameFromBGG use case
 */
public record Video (String externalId, String title, String category, String language, String link) {}
