package com.irostec.boardgamemanager.application.core.shared;

import com.irostec.boardgamemanager.application.core.shared.includeboardgame.exception.IncludeBoardGameException;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.input.RequestToIncludeBoardGame;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.output.BoardGameInclusionResult;

public interface IncludeBoardGame {

     BoardGameInclusionResult execute(RequestToIncludeBoardGame requestToIncludeBoardGame)
        throws IncludeBoardGameException;

}
