package com.irostec.boardgamemanager.application.core.shared.includeboardgame.error;

public record DatabaseError(Throwable cause) implements IncludeBoardGameError {
}
