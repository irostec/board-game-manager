package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output;

/**
 * Video
 * A video about a board game, as represented in the FetchBoardGameFromBGG use case
 */
public record Video (String externalId, String title, String category, String language, String link) {}
