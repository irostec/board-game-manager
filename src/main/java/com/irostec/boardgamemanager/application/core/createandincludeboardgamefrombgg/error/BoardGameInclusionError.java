package com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error;

import com.irostec.boardgamemanager.application.core.shared.includeboardgame.error.IncludeBoardGameError;

public record BoardGameInclusionError(IncludeBoardGameError source) implements CreateAndIncludeBoardGameFromBGGError {
}
