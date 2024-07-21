package com.irostec.boardgamemanager.application.core.shared;

import com.irostec.boardgamemanager.application.core.api.transaction.Transaction1;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.error.IncludeBoardGameError;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;
import io.vavr.control.Either;

public interface IncludeBoardGameService extends Transaction1<IncludeBoardGameError, BoardGameInclusionResult, RequestToIncludeBoardGame>  {

    Either<IncludeBoardGameError, BoardGameInclusionResult> execute(RequestToIncludeBoardGame requestToIncludeBoardGame);

}
