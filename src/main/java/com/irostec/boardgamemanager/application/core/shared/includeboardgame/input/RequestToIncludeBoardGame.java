package com.irostec.boardgamemanager.application.core.shared.includeboardgame.input;

public record RequestToIncludeBoardGame(long userId, long boardGameId, String reason) {
}
