package com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error;

import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.GetCurrentUserError;

public record UserRetrievalError(GetCurrentUserError source) implements CreateAndIncludeBoardGameFromBGGError {
}
